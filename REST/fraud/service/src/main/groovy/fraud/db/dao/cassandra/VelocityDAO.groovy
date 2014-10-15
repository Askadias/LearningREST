package fraud.db.dao.cassandra

import com.datastax.driver.core.querybuilder.QueryBuilder
import com.datastax.driver.core.utils.UUIDs
import com.datastax.driver.mapping.EntityTypeParser
import com.datastax.driver.mapping.meta.EntityFieldMetaData
import com.datastax.driver.mapping.meta.EntityTypeMetadata
import common.status.pojo.ComponentStatus
import fraud.db.dao.IVelocityDAO
import fraud.rest.v1.velocity.History
import fraud.rest.v1.velocity.Metric
import fraud.rest.v1.velocity.PartitionKey
import fraud.rest.v1.velocity.Transaction
import org.joda.time.DateTime

import static com.datastax.driver.core.querybuilder.QueryBuilder.*

/**
 * BlackLists DAO implementation
 */
class VelocityDAO extends BaseCassandraDAO implements IVelocityDAO {

    @Override
    void logTransaction(final Transaction transaction) {
        mappingSession.save(transaction)
    }

    @Override
    void logData(final History historicalData) {
        mappingSession.save(historicalData)
    }

    @Override
    Set<UUID> getHistoricalIDs(final PartitionKey id, final Long period) {
        EntityTypeMetadata emeta = EntityTypeParser.getEntityMetadata(History.class)
        EntityFieldMetaData typeMeta = emeta.getFieldMetadata('metricType')
        EntityFieldMetaData valueMeta = emeta.getFieldMetadata('metricValue')
        EntityFieldMetaData tranMeta = emeta.getFieldMetadata('transactionID')
        return mappingSession.session.execute(select().column(tranMeta.columnName).from(mappingSession.keyspace, emeta.tableName)
                .where(eq(typeMeta.columnName, id.metricType))
                .and(eq(valueMeta.columnName, id.metricValue))
                .and(gt(tranMeta.columnName, UUIDs.startOf(DateTime.now().millis - period))))
                .collect{it.getUUID(tranMeta.columnName)}.toSet();
    }

    @Override
    List<Transaction> getHistoricalData(final Set<UUID> transactionIDs) {
        EntityTypeMetadata emeta = EntityTypeParser.getEntityMetadata(Transaction.class)
        EntityFieldMetaData idMeta = emeta.getFieldMetadata('transactionID')
        //noinspection UnnecessaryQualifiedReference
        return mappingSession.getByQuery(Transaction.class,
                select().all().from(mappingSession.keyspace, emeta.tableName)
                        .where(QueryBuilder.in(idMeta.columnName, transactionIDs.toArray())))
    }

    @Override
    void saveMetric(final Metric metric) {
        mappingSession.saveAsync(metric)
    }

    @Override
    List<Metric> getMetrics(final PartitionKey id) {
        EntityTypeMetadata emeta = EntityTypeParser.getEntityMetadata(Metric.class)
        EntityFieldMetaData typeMeta = emeta.getFieldMetadata('metricType')
        EntityFieldMetaData valueMeta = emeta.getFieldMetadata('metricValue')
        mappingSession.getByQuery(Metric.class,
                select().all().from(mappingSession.keyspace, emeta.tableName)
                        .where(eq(typeMeta.columnName, id.metricType))
                        .and(eq(valueMeta.columnName, id.metricValue)))
    }

    /*@Override
    void saveBatchOfMetrics(final List<Metric> metrics) {
        MappingSession.BatchExecutor batchExecutor = mappingSession.withBatch()
        metrics.each { batchExecutor.save(it) }
        batchExecutor.executeAsync()
    }

    @Override
    void saveBatchOfData(final List<History> dataList) {
        MappingSession.BatchExecutor batchExecutor = mappingSession.withBatch()
        dataList.each { batchExecutor.save(it) }
        batchExecutor.executeAsync()
    }*/

    @Override
    ComponentStatus getStatus() {
        new ComponentStatus()
    }
}
