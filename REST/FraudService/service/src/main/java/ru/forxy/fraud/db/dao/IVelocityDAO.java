package ru.forxy.fraud.db.dao;

import ru.forxy.common.status.ISystemStatusComponent;
import ru.forxy.fraud.rest.v1.velocity.VelocityData;
import ru.forxy.fraud.rest.v1.velocity.VelocityDataCompositeKey;
import ru.forxy.fraud.rest.v1.velocity.VelocityMetric;
import ru.forxy.fraud.rest.v1.velocity.VelocityMetricCompositeKey;
import ru.forxy.fraud.rest.v1.velocity.VelocityPartitionKey;

import java.util.Date;
import java.util.List;

/**
 * Data Access Object for fraud database to manipulate BlackLists.
 */
public interface IVelocityDAO extends ISystemStatusComponent {

    List<VelocityMetric> getMoreMetrics(final String metricType, final String metricValue, final int limit);

    List<VelocityData> getMoreData(final String metricType, final String metricValue,
                                   final String relatedMetricType, final Date createDate, final int limit);

    List<VelocityMetric> getMetrics(final VelocityPartitionKey id);

    List<VelocityData> getDataList(final VelocityPartitionKey id);

    VelocityMetric getMetric(final VelocityMetricCompositeKey key);

    VelocityData getData(final VelocityDataCompositeKey key);

    List<VelocityData> getMetricDataForPeriod(final VelocityPartitionKey id, final String relatedMetricType,
                                              final Long period);

    void saveMetric(final VelocityMetric metric);

    void saveData(final VelocityData data);
}

