package ru.forxy.fraud.db.dao.redis

import org.springframework.data.redis.core.RedisTemplate
import ru.forxy.fraud.rest.v1.velocity.redis.Aggregation
import ru.forxy.fraud.rest.v1.velocity.redis.VData
import ru.forxy.fraud.rest.v1.velocity.redis.VKey

/**
 * Created by Tiger on 24.09.14.
 */
class VelocityDAO implements IRedisVelocityDAO {
    RedisTemplate<String, VData> velocityData;
    RedisTemplate<String, Double> velocityMetrics;

    void logData(VKey k, String relatedMetricData) {
        Long now = new Date().time
        velocityData.opsForZSet().add(toKey(k),
                new VData(value: relatedMetricData, timestamp: now), now);
    }

    List<String> getHistoricalData(VKey k, Long period) {
        Long now = new Date().time
        velocityData.opsForZSet().rangeByScore(toKey(k), now - period, now).collect { it.value }
    }

    void saveMetric(VKey k, Aggregation type, Double aggregatedValue) {
        velocityMetrics.opsForHash().put("${toKey(k)}:metrics", type, aggregatedValue)
    }

    Map<Aggregation, Double> getMetrics(VKey k) {
        velocityMetrics.opsForHash().entries("${toKey(k)}:metrics")
    }

    private static String toKey(VKey k) {
        "$k.metricType:$k.metricValue:$k.relatedMetricType"
    }
}
