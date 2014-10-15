package fraud.db.dao

import common.status.ISystemStatusComponent
import fraud.rest.v1.velocity.*

/**
 * Data Access Object for fraud database to manipulate BlackLists.
 */
interface IVelocityDAO extends ISystemStatusComponent {

    void logTransaction(final Transaction transaction)

    void logData(History historicalData)

    Set<UUID> getHistoricalIDs(PartitionKey id, Long period)

    List<Transaction> getHistoricalData(Set<UUID> transactionIDs)

    void saveMetric(Metric metric)

    List<Metric> getMetrics(PartitionKey id);
}

