package fraud.db.dao

import common.status.ISystemStatusComponent
import fraud.rest.v1.velocity.History
import fraud.rest.v1.velocity.Metric
import fraud.rest.v1.velocity.PartitionKey
import fraud.rest.v1.velocity.TransactionData

/**
 * Data Access Object for fraud database to manipulate BlackLists.
 */
interface ICassandraVelocityDAO extends ISystemStatusComponent {

    void logTransaction(final TransactionData transaction)

    void logData(final History historicalData)

    Set<UUID> getHistoricalIDs(final PartitionKey id, final Long period)

    Set<UUID> getHistoricalIDs(final PartitionKey id, final Long dateStart, final Long dateEnd)

    Set<UUID> getHistoricalIDs(final PartitionKey id, final Long dateStart, final Long dateEnd,
                               final UUID startID, final UUID endID)

    List<TransactionData> getHistoricalData(final Set<UUID> transactionIDs)

    void saveMetric(final Metric metric)

    List<Metric> getMetrics(final PartitionKey id);
}

