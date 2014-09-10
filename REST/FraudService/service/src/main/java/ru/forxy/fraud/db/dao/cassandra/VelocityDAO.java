package ru.forxy.fraud.db.dao.cassandra;

import ru.forxy.common.status.pojo.ComponentStatus;
import ru.forxy.fraud.db.dao.IVelocityDAO;
import ru.forxy.fraud.rest.v1.velocity.VelocityData;
import ru.forxy.fraud.rest.v1.velocity.VelocityMetric;
import ru.forxy.fraud.rest.v1.velocity.VelocityPartitionKey;

import java.util.List;

/**
 * BlackLists DAO implementation
 */
public class VelocityDAO extends BaseCassandraDAO implements IVelocityDAO {

    @Override
    public List<VelocityMetric> getMoreMetrics(String metricType, String value, int limit) {
        if (metricType != null && value != null) {
            return mappingSession.getByQuery(VelocityMetric.class,
                    "select * from velocity " +
                            "where token (metric_type, value) > " +
                            "token('" + metricType + "','" + value + "') " +
                            "limit " + limit);
        } else {
            return mappingSession.getByQuery(VelocityMetric.class,
                    "select * from velocity limit " + limit);
        }
    }

    @Override
    public List<VelocityData> getMoreData(String metricType, String value, int limit) {
        if (metricType != null && value != null) {
            return mappingSession.getByQuery(VelocityData.class,
                    "select * from velocity_data " +
                            "where token (metric_type, value) > " +
                            "token('" + metricType + "','" + value + "') " +
                            "limit " + limit);
        } else {
            return mappingSession.getByQuery(VelocityData.class,
                    "select * from velocity limit " + limit);
        }
    }

    @Override
    public VelocityMetric getMetric(VelocityPartitionKey id) {
        return getMetric(new VelocityMetric().new CompositeKey(id));
    }

    @Override
    public VelocityData getData(VelocityPartitionKey id) {
        return getData(new VelocityData().new CompositeKey(id));
    }

    @Override
    public VelocityMetric getMetric(VelocityMetric.CompositeKey key) {
        return mappingSession.get(VelocityMetric.class, key);
    }

    @Override
    public VelocityData getData(VelocityData.CompositeKey key) {
        return mappingSession.get(VelocityData.class, key);
    }

    @Override
    public void saveMetric(VelocityMetric metric) {
        mappingSession.save(metric);
    }

    @Override
    public void saveData(VelocityData data) {
        mappingSession.save(data);
    }

    @Override
    public ComponentStatus getStatus() {
        return new ComponentStatus();
    }
}
