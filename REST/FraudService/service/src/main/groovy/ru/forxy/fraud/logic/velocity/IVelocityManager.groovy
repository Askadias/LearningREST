package ru.forxy.fraud.logic.velocity

import ru.forxy.fraud.rest.v1.velocity.AggregationType
import ru.forxy.fraud.rest.v1.velocity.VelocityData
import ru.forxy.fraud.rest.v1.velocity.VelocityMetric

/**
 * Black lists manipulation logic API
 */
interface IVelocityManager {

    List<VelocityMetric> check(final Map<String, String> metrics)

    List<VelocityMetric> check2(final Map<String, String> metrics)

    List<VelocityMetric> getMoreMetricsFrom(final String metricType, final String metricValue)

    List<VelocityMetric> getMoreMetricsFrom(final String metricType, final String metricValue, final int limit)

    List<VelocityMetric> getMetrics(final String metricType, final String metricValue)

    VelocityMetric getMetric(final String metricType, final String metricValue, final String relatedMetricType,
                             final AggregationType aggregationType)

    void addMetric(final VelocityMetric metric)

    void updateMetric(final VelocityMetric metric)


    List<VelocityData> getMoreDataFrom(final String metricType, final String metricValue,
                                       final String relatedMetricType, final Date createDate)

    List<VelocityData> getMoreDataFrom(final String metricType, final String metricValue,
                                       final String relatedMetricType, final Date createDate, final int limit)

    List<VelocityData> getDataList(final String metricType, final String metricValue)

    VelocityData getData(final String metricType, final String metricValue, final String relatedMetricType,
                         final Date createDate)

    void addData(final VelocityData data)

    void updateData(final VelocityData data)
}
