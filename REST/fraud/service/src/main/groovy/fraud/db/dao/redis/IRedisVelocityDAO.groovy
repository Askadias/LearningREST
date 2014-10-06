package fraud.db.dao.redis

import fraud.rest.v1.velocity.redis.Aggregation

/**
 * Velocity DAO vor redis DataSource
 */
interface IRedisVelocityDAO {

    void logData(String key, String relatedMetricData)

    List<String> getHistoricalData(String key, Long period)

    void saveMetric(String key, Aggregation type, Double aggregatedValue)

    Map<Aggregation, Double> getMetrics(String key)
}