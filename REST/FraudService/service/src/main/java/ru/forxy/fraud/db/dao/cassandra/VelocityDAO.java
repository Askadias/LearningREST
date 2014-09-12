package ru.forxy.fraud.db.dao.cassandra;

import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.mapping.EntityTypeParser;
import com.datastax.driver.mapping.meta.EntityFieldMetaData;
import com.datastax.driver.mapping.meta.EntityTypeMetadata;
import ru.forxy.common.status.pojo.ComponentStatus;
import ru.forxy.fraud.db.dao.IVelocityDAO;
import ru.forxy.fraud.rest.v1.velocity.VelocityData;
import ru.forxy.fraud.rest.v1.velocity.VelocityDataCompositeKey;
import ru.forxy.fraud.rest.v1.velocity.VelocityMetric;
import ru.forxy.fraud.rest.v1.velocity.VelocityMetricCompositeKey;
import ru.forxy.fraud.rest.v1.velocity.VelocityPartitionKey;

import java.util.Date;
import java.util.List;

import static com.datastax.driver.core.querybuilder.QueryBuilder.eq;
import static com.datastax.driver.core.querybuilder.QueryBuilder.gt;
import static com.datastax.driver.core.querybuilder.QueryBuilder.token;

/**
 * BlackLists DAO implementation
 */
public class VelocityDAO extends BaseCassandraDAO implements IVelocityDAO {

    @Override
    public List<VelocityMetric> getMoreMetrics(final String metricType, final String metricValue, final int limit) {
        EntityTypeMetadata emeta = EntityTypeParser.getEntityMetadata(VelocityMetric.class);
        if (metricType != null && metricValue != null) {
            EntityFieldMetaData typeMeta = emeta.getFieldMetadata("metricType");
            EntityFieldMetaData valueMeta = emeta.getFieldMetadata("metricValue");
            return mappingSession.getByQuery(VelocityMetric.class,
                    QueryBuilder.select().all().from(mappingSession.getKeyspace(), emeta.getTableName())
                            .where(gt(token(typeMeta.getColumnName(), valueMeta.getColumnName()),
                                    token(metricType, metricValue))).limit(limit));
        } else {
            return mappingSession.getByQuery(VelocityMetric.class,
                    QueryBuilder.select().all().from(mappingSession.getKeyspace(), emeta.getTableName()).limit(limit));
        }
    }

    @Override
    public List<VelocityData> getMoreData(final String metricType, final String metricValue,
                                          final String relatedMetricType, final Date createDate, final int limit) {
        EntityTypeMetadata emeta = EntityTypeParser.getEntityMetadata(VelocityData.class);
        if (metricType != null && metricValue != null && relatedMetricType != null && createDate != null) {
            return mappingSession.getByQuery(VelocityData.class,
                    "select * from velocity_data " +
                            "where token(metric_type,metric_value) >= token('"+metricType+"','"+metricValue+"') " +
                            "and related_metric_type = '" + relatedMetricType + "' " +
                            "and create_date > " + createDate.getTime() + " " +
                            "limit " + limit + " allow filtering");
            /*EntityFieldMetaData typeMeta = emeta.getFieldMetadata("metricType");
            EntityFieldMetaData valueMeta = emeta.getFieldMetadata("metricValue");
            return mappingSession.getByQuery(VelocityData.class,
                    QueryBuilder.select().all().from(mappingSession.getKeyspace(), emeta.getTableName())
                            .where(gt(token(typeMeta.getColumnName(), valueMeta.getColumnName()),
                                    token(metricType, metricValue))).limit(limit));*/
        } else {
            return mappingSession.getByQuery(VelocityData.class,
                    QueryBuilder.select().all().from(mappingSession.getKeyspace(), emeta.getTableName()).limit(limit));
        }
    }

    @Override
    public List<VelocityMetric> getMetrics(final VelocityPartitionKey id) {
        EntityTypeMetadata emeta = EntityTypeParser.getEntityMetadata(VelocityMetric.class);
        EntityFieldMetaData typeMeta = emeta.getFieldMetadata("metricType");
        EntityFieldMetaData valueMeta = emeta.getFieldMetadata("metricValue");
        return mappingSession.getByQuery(VelocityMetric.class,
                QueryBuilder.select().all().from(mappingSession.getKeyspace(), emeta.getTableName())
                        .where(eq(typeMeta.getColumnName(), id.getMetricType()))
                        .and(eq(valueMeta.getColumnName(), id.getMetricValue())));
    }

    @Override
    public List<VelocityData> getDataList(final VelocityPartitionKey id) {
        EntityTypeMetadata emeta = EntityTypeParser.getEntityMetadata(VelocityData.class);
        EntityFieldMetaData typeMeta = emeta.getFieldMetadata("metricType");
        EntityFieldMetaData valueMeta = emeta.getFieldMetadata("metricValue");
        return mappingSession.getByQuery(VelocityData.class,
                QueryBuilder.select().all().from(mappingSession.getKeyspace(), emeta.getTableName())
                        .where(eq(typeMeta.getColumnName(), id.getMetricType()))
                        .and(eq(valueMeta.getColumnName(), id.getMetricValue())));
    }

    @Override
    public VelocityMetric getMetric(final VelocityMetricCompositeKey key) {
        return mappingSession.get(VelocityMetric.class, key);
    }

    @Override
    public VelocityData getData(final VelocityDataCompositeKey key) {
        return mappingSession.get(VelocityData.class, key);
    }

    @Override
    public List<VelocityData> getMetricDataForPeriod(final VelocityPartitionKey id, final String relatedMetricType,
                                         final Long period) {
        EntityTypeMetadata emeta = EntityTypeParser.getEntityMetadata(VelocityData.class);
        EntityFieldMetaData typeMeta = emeta.getFieldMetadata("metricType");
        EntityFieldMetaData valueMeta = emeta.getFieldMetadata("metricValue");
        EntityFieldMetaData relatedMetricTypeMeta = emeta.getFieldMetadata("relatedMetricType");
        EntityFieldMetaData createDateMeta = emeta.getFieldMetadata("createDate");
        Date startDate = new Date(new Date().getTime() - period);
        return mappingSession.getByQuery(VelocityData.class,
                QueryBuilder.select().all().from(mappingSession.getKeyspace(), emeta.getTableName())
                        .where(eq(typeMeta.getColumnName(), id.getMetricType()))
                        .and(eq(valueMeta.getColumnName(), id.getMetricValue()))
                        .and(eq(relatedMetricTypeMeta.getColumnName(), relatedMetricType))
                        .and(gt(createDateMeta.getColumnName(), startDate)));
    }

    @Override
    public void saveMetric(final VelocityMetric metric) {
        mappingSession.save(metric);
    }

    @Override
    public void saveData(final VelocityData data) {
        mappingSession.save(data);
    }

    @Override
    public ComponentStatus getStatus() {
        return new ComponentStatus();
    }
}
