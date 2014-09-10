package ru.forxy.fraud.db.dao;

import ru.forxy.common.status.ISystemStatusComponent;
import ru.forxy.fraud.rest.v1.velocity.VelocityData;
import ru.forxy.fraud.rest.v1.velocity.VelocityMetric;
import ru.forxy.fraud.rest.v1.velocity.VelocityPartitionKey;

import java.util.List;

/**
 * Data Access Object for fraud database to manipulate BlackLists.
 */
public interface IVelocityDAO extends ISystemStatusComponent {

    List<VelocityMetric> getMoreMetrics(final String metricType, final String value, final int limit);

    List<VelocityData> getMoreData(final String metricType, final String value, final int limit);

    VelocityMetric getMetric(final VelocityPartitionKey id);

    VelocityData getData(final VelocityPartitionKey id);

    VelocityMetric getMetric(final VelocityMetric.CompositeKey key);

    VelocityData getData(final VelocityData.CompositeKey key);

    void saveMetric(final VelocityMetric metric);

    void saveData(final VelocityData data);
}

