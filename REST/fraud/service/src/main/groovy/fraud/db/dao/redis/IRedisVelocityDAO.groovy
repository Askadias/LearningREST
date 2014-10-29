package fraud.db.dao.redis

import fraud.api.v1.velocity.Aggregation
import fraud.api.v1.velocity.Transaction

/**
 * Velocity DAO vor redis DataSource
 */
interface IRedisVelocityDAO {

    void logData(Map<String, String[]> newData)

    Set<String> getHistoricalIDs(String key, Long period)

    Set<String> getHistoricalIDs(final String key, final Long startDateMillis, final Long endDateMillis)

    List<String> getHistoricalData(String dataType, Set<String> transactionIDs)

    List<Transaction> getHistoricalData(Set<String> transactionIDs)

    void saveMetric(String key, Aggregation type, Double aggregatedValue)

    Map<Aggregation, Double> getMetrics(String key)
}