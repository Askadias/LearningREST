package fraud.db.dao

import common.status.ISystemStatusComponent
import fraud.rest.v1.velocity.*

/**
 * Data Access Object for fraud database to manipulate BlackLists.
 */
interface ICassandraVelocityDAO extends ISystemStatusComponent {

    void logTransaction(final TransactionData transaction)

    void logData(final History historicalData)

    Set<UUID> getHistoricalIDs(final PartitionKey id, final Long period)

    Set<UUID> getHistoricalIDs(final PartitionKey id, final Long dateStart, final Long dateEnd)

    List<TransactionData> getHistoricalData(final Set<UUID> transactionIDs)

    void saveMetric(final Metric metric)

    List<Metric> getMetrics(final PartitionKey id);
}

