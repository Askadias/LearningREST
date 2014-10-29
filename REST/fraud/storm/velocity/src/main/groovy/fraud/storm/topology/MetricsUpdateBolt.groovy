package fraud.storm.topology

import backtype.storm.task.OutputCollector
import backtype.storm.task.TopologyContext
import backtype.storm.topology.OutputFieldsDeclarer
import backtype.storm.topology.base.BaseRichBolt
import backtype.storm.tuple.Tuple
import fraud.api.v1.velocity.AggregationConfig
import fraud.api.v1.velocity.Transaction
import fraud.api.v1.velocity.VelocityConfig
import groovyx.gpars.GParsPool
import org.joda.time.DateTime
import redis.clients.jedis.Jedis
import redis.clients.jedis.JedisPool
import redis.clients.jedis.JedisPoolConfig

import java.util.concurrent.ForkJoinPool

/**
 * Created by Tiger on 29.10.14.
 */
class MetricsUpdateBolt extends BaseRichBolt {

    private OutputCollector collector;
    final String host;
    final int port;
    JedisPool pool;

    public MetricsUpdateBolt(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    public void prepare(Map conf, TopologyContext context, OutputCollector collector) {
        this.collector = collector;
        pool = new JedisPool(new JedisPoolConfig(), host, port);
    }

    @Override
    void execute(final Tuple tuple) {
        List<VelocityConfig> configs = tuple.getValue(0) as List<VelocityConfig>
        Transaction transaction = tuple.getValue(1) as Transaction;
        Long now = DateTime.now().millis
        Jedis jedis = pool.getResource()
        try {
            withPool { ForkJoinPool pool ->
                configs?.eachParallel { VelocityConfig config ->
                    withExistingPool(pool) {
                        GParsPool.runForkJoin(config, config.primaryMetrics.asList(), 0, null, null, transaction) {
                            conf, List<String> primaryMetrics, int index, Set<String> tranIds, String key, tran ->
                                if (index < primaryMetrics.size()) {
                                    String metric = primaryMetrics[index]
                                    tran.data[metric].each {
                                        String newKey = "$metric:$it"
                                        String fullKey = key ? "$key:$newKey" : newKey

                                        Set<String> tranIDsIntersection = []
                                        Set<String> newIds =
                                                jedis.zrangeByScore("$newKey:history", now - config.period, now)
                                        tranIDsIntersection = tranIds ? tranIds.intersect(newIds) : newIds

                                        if (index + 1 < primaryMetrics.size()) {
                                            forkOffChild(config, primaryMetrics, index + 1, tranIDsIntersection, fullKey, tran)
                                        } else {
                                            config.aggregationConfigs?.each { AggregationConfig aggConf ->
                                                List<String> history = []
                                                tranIDsIntersection.each {
                                                    history += jedis.lrange(
                                                            "transaction:$it:data:$aggConf.secondaryMetric" as String,
                                                            0, -1);
                                                }
                                                jedis.hset("metrics:$fullKey:$aggConf.secondaryMetric" as String,
                                                        aggConf.aggregation as String,
                                                        aggConf.aggregation.apply(history) as String)
                                            }
                                        }
                                    }
                                }
                        }
                    }
                }
            }
        } finally {
            pool.returnResource(jedis)
        }
        collector.ack(tuple);
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
    }
}