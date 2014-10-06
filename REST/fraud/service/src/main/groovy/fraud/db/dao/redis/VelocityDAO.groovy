package fraud.db.dao.redis

import fraud.rest.v1.velocity.redis.Aggregation
import org.joda.time.DateTime
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.transaction.annotation.Transactional

/**
 * Created by Tiger on 24.09.14.
 */
class VelocityDAO implements IRedisVelocityDAO {
    StringRedisTemplate redis;

    @Transactional
    void logData(String key, String relatedMetricData) {
        Long now = DateTime.now().millis
        String id = redis.opsForValue().get("$key:nextId".toString())
        if (!id) {
            id = '1'
            redis.opsForValue().set("$key:nextId".toString(), id)
        }
        redis.opsForValue().set("$key:data:$id".toString(), relatedMetricData)
        redis.opsForValue().increment("$key:nextId".toString(), 1)
        redis.opsForZSet().add("$key:history".toString(), id, now)
    }

    List<String> getHistoricalData(String key, Long period) {
        Long now = DateTime.now().millis
        Set<String> historyIDs = redis.opsForZSet().rangeByScore("$key:history".toString(), now - period, now)
        return !historyIDs ? [] : redis.opsForValue().multiGet(historyIDs.collect { "$key:data:$it".toString() })
    }

    void saveMetric(String key, Aggregation type, Double aggregatedValue) {
        redis.opsForHash().put("$key:metrics".toString(), type as String, aggregatedValue as String)
    }

    Map<Aggregation, Double> getMetrics(String key) {
        def metrics = [:]
        redis.opsForHash().entries("$key:metrics".toString()).each { k, v ->
            metrics << [(Aggregation.valueOf(k.toString())): Double.valueOf(v.toString())]
        }
        return metrics
    }
}
