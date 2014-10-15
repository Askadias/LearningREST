package fraud.db.dao.redis

import fraud.rest.v1.velocity.Aggregation

/**
 * Velocity DAO vor redis DataSource
 */
interface IRedisVelocityDAO {

    void logData(Map<String, String[]> newData)

    Set<String> getHistoricalIDs(String key, Long period)
    List<String> getHistoricalData(String dataType, Set<String> transactionIDs)

    void saveMetric(String key, Aggregation type, Double aggregatedValue)

    Map<String, String> getMetrics(String key)
}