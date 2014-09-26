package fraud.db.dao.redis

import fraud.rest.v1.velocity.redis.Aggregation
import fraud.rest.v1.velocity.redis.VKey

/**
 * Created by Tiger on 24.09.14.
 */
interface IRedisVelocityDAO {

    void logData(VKey fixedMetricKey, String relatedMetricData)

    List<String> getHistoricalData(VKey metric, Long period)

    void saveMetric(VKey fixedMetricKey, Aggregation type, Double aggregatedValue)

    Map<Aggregation, Double> getMetrics(VKey metricKey)
}