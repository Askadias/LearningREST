package fraud.db.dao.redis

import fraud.rest.v1.velocity.Aggregation
import org.joda.time.DateTime
import org.springframework.dao.DataAccessException
import org.springframework.data.redis.connection.RedisConnection
import org.springframework.data.redis.connection.StringRedisConnection
import org.springframework.data.redis.core.RedisCallback
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.transaction.annotation.Transactional

/**
 * Created by Tiger on 24.09.14.
 */
class RedisVelocityDAO implements IRedisVelocityDAO {
    StringRedisTemplate redis;

    @Transactional
    @Override
    void logData(final Map<String, String[]> newData) {
        Long now = DateTime.now().millis
        Long transactionID = redis.opsForValue().increment('id:transactions', 1)
        redis.executePipelined(new RedisCallback<Object>() {
            @Override
            Object doInRedis(final RedisConnection connection) throws DataAccessException {
                StringRedisConnection c = connection as StringRedisConnection
                newData.each { metricType, metricValues ->
                    c.lPush("transaction:$transactionID:$metricType".toString(), metricValues as String[])
                    metricValues?.each { metricValue ->
                        c.zAdd("$metricType:$metricValue:history".toString(), now, transactionID as String)
                    }
                }
                return null;
            }
        })
    }

    @Override
    Set<String> getHistoricalIDs(final String key, final Long period) {
        Long now = DateTime.now().millis
        return redis.boundZSetOps(key).rangeByScore(now - period, now)
    }

    @Override
    List<String> getHistoricalData(final String dataType, final Set<String> transactionIDs) {
        List<String> history = []
        transactionIDs.each {
            history += redis.boundListOps("transaction:$it:$dataType".toString())range(0, -1);
        }
        return history
    }

    void saveMetric(String key, Aggregation type, Double aggregatedValue) {
        redis.opsForHash().put(key, type as String, aggregatedValue as String)
    }

    Map<String, String> getMetrics(String key) {
        redis.opsForHash().entries(key)
    }
}
