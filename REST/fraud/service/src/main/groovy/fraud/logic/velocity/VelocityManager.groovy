package fraud.logic.velocity

import com.datastax.driver.core.utils.UUIDs
import fraud.db.dao.IVelocityDAO
import fraud.db.dao.redis.IRedisVelocityDAO
import fraud.rest.v1.velocity.*
import fraud.util.DBCache
import groovyx.gpars.GParsPool
import jsr166y.ForkJoinPool
import org.joda.time.DateTime
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import java.util.concurrent.ConcurrentHashMap

import static groovyx.gpars.GParsPool.withExistingPool
import static groovyx.gpars.GParsPool.withPool

/**
 * Implementation class for BlackListService business logic
 */
class VelocityManager implements IVelocityManager {

    private static final int DEFAULT_PAGE_SIZE = 30

    private static final Logger LOGGER = LoggerFactory.getLogger(VelocityManager.class)

    IVelocityDAO velocityDAO
    IRedisVelocityDAO redisVelocityDAO;
    DBCache dbCache
    private final static java.util.concurrent.ForkJoinPool FJP = new java.util.concurrent.ForkJoinPool(2)

    @Override
    def checkCassandraAsync(final Map<String, String[]> velocityRQ) {
        def velocityMetrics = new ConcurrentHashMap<Map<String, String>, Map<Aggregation, Double>>()
        withPool { ForkJoinPool pool ->
            dbCache.configs.collectParallel { VelocityConfig config ->
                withExistingPool(pool) {
                    GParsPool.runForkJoin(config, config.primaryMetrics.asList(), 0, [:], null, null, velocityRQ) {
                        VelocityConfig conf, List<String> primaryMetrics, int index, Map<String, String> key,
                        String keyType, String keyValue, Map<String, String[]> rq ->
                            def metrics = [:]
                            if (index < primaryMetrics.size()) {
                                String metric = primaryMetrics[index]
                                rq[metric].each {
                                    key << [(metric): it]
                                    String fullKeyType = keyType ? "$keyType:$metric" : metric
                                    String fullKeyValue = keyValue ? "$keyValue:$it" : it
                                    if (index + 1 < primaryMetrics.size()) {
                                        forkOffChild(conf, primaryMetrics, index + 1, key.clone(), fullKeyType, fullKeyValue, rq)
                                    } else {
                                        def velocityKey = key.clone()
                                        metrics << [(velocityKey): [:]]
                                        List<Metric> metricsLst = velocityDAO.getMetrics(new PartitionKey(
                                                metricType: fullKeyType, metricValue: fullKeyValue))
                                        metricsLst.each {
                                            if (!metrics[velocityKey][(it.key.secondaryMetric)]) {
                                                metrics[velocityKey] << [(it.key.secondaryMetric): [:]]
                                            }
                                            metrics[velocityKey][(it.key.secondaryMetric)] <<
                                                    [(it.key.aggregationType): it.aggregatedValue]
                                            /*metrics[velocityKey] << [(it.key.secondaryMetric):
                                                                 [(it.key.aggregationType): it.aggregatedValue]]*/
                                        }
                                    }
                                }
                                childrenResults?.each {
                                    metrics += it
                                }
                            }
                            return metrics
                    }
                }
            }.each {
                velocityMetrics += it
            }
        }
        FJP.submit(new Runnable() {
            void run() {
                updateMetricsCassandra(velocityRQ)
            }
        })
        /*withPool() {
            Closure updateMetricsFn = { updateMetricsCassandra(velocityRQ) }
            updateMetricsFn.callAsync()
        }*/
        return velocityMetrics
    }

    void updateMetricsCassandra(Map<String, String[]> velocityRQ) {
        logCassandraData(velocityRQ)
        withPool { ForkJoinPool pool ->
            dbCache.configs?.eachParallel { VelocityConfig config ->
                withExistingPool(pool) {
                    GParsPool.runForkJoin(config, config.primaryMetrics.asList(), 0, null, null, null, velocityRQ) {
                        conf, List<String> primaryMetrics, int index, Set<UUID> tranIds, String keyType, String keyValue, rq ->
                            if (index < primaryMetrics.size()) {
                                String metric = primaryMetrics[index]
                                rq[metric].each {
                                    String fullKeyType = keyType ? "$keyType:$metric" : metric
                                    String fullKeyValue = keyValue ? "$keyValue:$it" : it

                                    Set<UUID> newIds = velocityDAO.getHistoricalIDs(
                                            new PartitionKey(metricType: metric, metricValue: it), config.period)
                                    Set<UUID> tranIDsIntersection = tranIds ? tranIds.intersect(newIds) : newIds

                                    if (index + 1 < primaryMetrics.size()) {
                                        forkOffChild(config, primaryMetrics, index + 1, tranIDsIntersection,
                                                fullKeyType, fullKeyValue, rq)
                                    } else {
                                        def transactions = velocityDAO.getHistoricalData(tranIDsIntersection)
                                        config.aggregationConfigs?.each { AggregationConfig aggConf ->

                                            List<String> history = transactions.collect {
                                                it.key.dataType == aggConf.secondaryMetric ? it.data : []
                                            }.flatten()
                                            velocityDAO.saveMetric(new Metric(
                                                    key: new Metric.MetricCompositeKey(
                                                            id: new PartitionKey(
                                                                    metricType: fullKeyType,
                                                                    metricValue: fullKeyValue),
                                                            secondaryMetric: aggConf.secondaryMetric,
                                                            aggregationType: aggConf.aggregation
                                                    ),
                                                    aggregatedValue: aggConf.aggregation.apply(history)
                                            ))
                                        }
                                    }
                                }
                            }
                    }
                }
            }
        }
    }

    void logCassandraData(final Map<String, String[]> velocityRQ) {
        UUID transactionID = UUIDs.startOf(DateTime.now().millis)
        velocityRQ.each { metricType, metricValues ->
            velocityDAO.logTransaction(new Transaction(
                    key: new Transaction.TransactionCompositeKey(
                            transactionID: transactionID,
                            dataType: metricType
                    ),
                    data: metricValues
            ))
            metricValues?.each { metricValue ->
                velocityDAO.logData(new History(
                        key: new History.HistoryCompositeKey(
                                id: new PartitionKey(metricType: metricType, metricValue: metricValue),
                                transactionID: transactionID
                        )
                ))
            }
        }
    }

    @Override
    def checkRedisAsync(Map<String, String[]> velocityRQ) {
        def velocityMetrics = [:]
        withPool { ForkJoinPool pool ->
            dbCache.configs.collectParallel { VelocityConfig config ->
                withExistingPool(pool) {
                    GParsPool.runForkJoin(config, config.primaryMetrics.asList(), 0, [:], null, velocityRQ) {
                        VelocityConfig conf, List<String> primaryMetrics, int index, Map<String, String> key,
                        String metricKey, Map<String, String[]> rq ->
                            def metrics = [:]
                            if (index < primaryMetrics.size()) {
                                String metric = primaryMetrics[index]
                                rq[metric].each {
                                    key << [(metric): it]
                                    String newKey = "$metric:$it"
                                    String fullKey = metricKey ? "$metricKey:$newKey" : newKey
                                    if (index + 1 < primaryMetrics.size()) {
                                        forkOffChild(conf, primaryMetrics, index + 1, key.clone(), fullKey, rq)
                                    } else {
                                        def velocityKey = key.clone()
                                        metrics << [(velocityKey): [:]]
                                        conf.aggregationConfigs.each {
                                            metrics[velocityKey] <<
                                                    [(it.secondaryMetric): redisVelocityDAO.getMetrics(
                                                            "$fullKey:metrics:$it.secondaryMetric".toString())]
                                        }
                                    }
                                }
                                childrenResults?.each {
                                    metrics += it
                                }
                            }
                            return metrics
                    }
                }
            }.each {
                velocityMetrics += it
            }
        }
        /*withPool() {
            Closure updateMetricsFn = { updateMetricsRedis(velocityRQ) }
            updateMetricsFn.callAsync()
        }*/
        FJP.submit(new Runnable() {
            void run() {
                updateMetricsRedis(velocityRQ)
            }
        })
        return velocityMetrics
    }

    void updateMetricsRedis(Map<String, String[]> velocityRQ) {
        redisVelocityDAO.logData(velocityRQ)
        withPool() { ForkJoinPool pool ->
            dbCache.configs?.eachParallel { VelocityConfig config ->
                withExistingPool(pool) {
                    GParsPool.runForkJoin(config, config.primaryMetrics.asList(), 0, null, null, velocityRQ) {
                        conf, List<String> primaryMetrics, int index, Set<String> tranIds, String key, rq ->
                            if (index < primaryMetrics.size()) {
                                String metric = primaryMetrics[index]
                                rq[metric].each {
                                    String newKey = "$metric:$it"
                                    String fullKey = key ? "$key:$newKey" : newKey

                                    Set<String> tranIDsIntersection = []
                                    Set<String> newIds = redisVelocityDAO.getHistoricalIDs("$newKey:history", config.period)
                                    tranIDsIntersection = tranIds ? tranIds.intersect(newIds) : newIds

                                    if (index + 1 < primaryMetrics.size()) {
                                        forkOffChild(config, primaryMetrics, index + 1, tranIDsIntersection, fullKey, rq)
                                    } else {
                                        config.aggregationConfigs?.each { AggregationConfig aggConf ->
                                            List<String> history = redisVelocityDAO.getHistoricalData(
                                                    aggConf.secondaryMetric, tranIDsIntersection)
                                            redisVelocityDAO.saveMetric(
                                                    "$fullKey:metrics:$aggConf.secondaryMetric".toString(),
                                                    aggConf.aggregation, aggConf.aggregation.apply(history))
                                        }
                                    }
                                }
                            }
                    }
                }
            }
        }
    }
}
