package ru.forxy.fraud.db.dao.cassandra

import com.datastax.driver.mapping.EntityTypeParser
import com.datastax.driver.mapping.MappingSession
import com.datastax.driver.mapping.meta.EntityFieldMetaData
import com.datastax.driver.mapping.meta.EntityTypeMetadata
import ru.forxy.common.status.pojo.ComponentStatus
import ru.forxy.fraud.db.dao.IVelocityDAO
import ru.forxy.fraud.rest.v1.velocity.*

import static com.datastax.driver.core.querybuilder.QueryBuilder.*

/**
 * BlackLists DAO implementation
 */
class VelocityDAO extends BaseCassandraDAO implements IVelocityDAO {

    @Override
    List<VelocityMetric> getMoreMetrics(final String metricType, final String metricValue, final int limit) {
        EntityTypeMetadata emeta = EntityTypeParser.getEntityMetadata(VelocityMetric.class)
        if (metricType != null && metricValue != null) {
            EntityFieldMetaData typeMeta = emeta.getFieldMetadata('metricType')
            EntityFieldMetaData valueMeta = emeta.getFieldMetadata('metricValue')
            return mappingSession.getByQuery(VelocityMetric.class,
                    select().all().from(mappingSession.getKeyspace(), emeta.getTableName())
                            .where(gt(token(typeMeta.getColumnName(), valueMeta.getColumnName()),
                            token(metricType, metricValue))).limit(limit))
        } else {
            return mappingSession.getByQuery(VelocityMetric.class,
                    select().all().from(mappingSession.getKeyspace(), emeta.getTableName()).limit(limit))
        }
    }

    @Override
    List<VelocityData> getMoreData(final String metricType, final String metricValue,
                                   final String relatedMetricType, final Date createDate, final int limit) {
        EntityTypeMetadata emeta = EntityTypeParser.getEntityMetadata(VelocityData.class)
        if (metricType != null && metricValue != null && relatedMetricType != null && createDate != null) {
            return mappingSession.getByQuery(VelocityData.class,
                    "select * from velocity_data " +
                            "where token(metric_type,metric_value) >= token('$metricType','$metricValue')" +
                            "and related_metric_type = '$relatedMetricType'" +
                            "and create_date > '$createDate.time'" +
                            "limit $limit allow filtering")
            /*EntityFieldMetaData typeMeta = emeta.getFieldMetadata('metricType')
            EntityFieldMetaData valueMeta = emeta.getFieldMetadata('metricValue')
            return mappingSession.getByQuery(VelocityData.class,
                    QueryBuilder.select().all().from(mappingSession.getKeyspace(), emeta.getTableName())
                            .where(gt(token(typeMeta.getColumnName(), valueMeta.getColumnName()),
                                    token(metricType, metricValue))).limit(limit))*/
        } else {
            return mappingSession.getByQuery(VelocityData.class,
                    select().all().from(mappingSession.keyspace, emeta.tableName).limit(limit))
        }
    }

    @Override
    List<VelocityMetric> getMetrics(final VelocityPartitionKey id) {
        EntityTypeMetadata emeta = EntityTypeParser.getEntityMetadata(VelocityMetric.class)
        EntityFieldMetaData typeMeta = emeta.getFieldMetadata('metricType')
        EntityFieldMetaData valueMeta = emeta.getFieldMetadata('metricValue')
        mappingSession.getByQuery(VelocityMetric.class,
                select().all().from(mappingSession.keyspace, emeta.tableName)
                        .where(eq(typeMeta.columnName, id.metricType))
                        .and(eq(valueMeta.columnName, id.metricValue)))
    }

    @Override
    List<VelocityData> getDataList(final VelocityPartitionKey id) {
        EntityTypeMetadata emeta = EntityTypeParser.getEntityMetadata(VelocityData.class)
        EntityFieldMetaData typeMeta = emeta.getFieldMetadata('metricType')
        EntityFieldMetaData valueMeta = emeta.getFieldMetadata('metricValue')
        mappingSession.getByQuery(VelocityData.class,
                select().all().from(mappingSession.keyspace, emeta.tableName)
                        .where(eq(typeMeta.columnName, id.metricType))
                        .and(eq(valueMeta.columnName, id.metricValue)))
    }

    @Override
    VelocityMetric getMetric(final VelocityMetricCompositeKey key) {
        return mappingSession.get(VelocityMetric.class, key)
    }

    @Override
    VelocityData getData(final VelocityDataCompositeKey key) {
        return mappingSession.get(VelocityData.class, key)
    }

    @Override
    List<VelocityData> getMetricDataForPeriod(final VelocityPartitionKey id, final String relatedMetricType,
                                              final Long period) {
        EntityTypeMetadata emeta = EntityTypeParser.getEntityMetadata(VelocityData.class)
        EntityFieldMetaData typeMeta = emeta.getFieldMetadata('metricType')
        EntityFieldMetaData valueMeta = emeta.getFieldMetadata('metricValue')
        EntityFieldMetaData relatedMetricTypeMeta = emeta.getFieldMetadata('relatedMetricType')
        EntityFieldMetaData createDateMeta = emeta.getFieldMetadata('createDate')
        Date startDate = new Date(new Date().time - period)
        mappingSession.getByQuery(VelocityData.class,
                select().all().from(mappingSession.keyspace, emeta.tableName)
                        .where(eq(typeMeta.columnName, id.metricType))
                        .and(eq(valueMeta.columnName, id.metricValue))
                        .and(eq(relatedMetricTypeMeta.columnName, relatedMetricType))
                        .and(gt(createDateMeta.columnName, startDate)))
    }

    @Override
    void saveMetric(final VelocityMetric metric) {
        mappingSession.save(metric)
    }

    @Override
    void saveBatchOfMetrics(final List<VelocityMetric> metrics) {
        MappingSession.BatchExecutor batchExecutor = mappingSession.withBatch()
        metrics.each { batchExecutor.save(it) }
        batchExecutor.executeAsync()
    }

    @Override
    void saveData(final VelocityData data) {
        mappingSession.save(data)
    }

    @Override
    void saveBatchOfData(final List<VelocityData> dataList) {
        MappingSession.BatchExecutor batchExecutor = mappingSession.withBatch()
        dataList.each { batchExecutor.save(it) }
        batchExecutor.executeAsync()
    }

    @Override
    ComponentStatus getStatus() {
        new ComponentStatus()
    }
}
