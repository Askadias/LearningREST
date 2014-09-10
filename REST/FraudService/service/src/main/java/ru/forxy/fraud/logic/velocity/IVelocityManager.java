package ru.forxy.fraud.logic.velocity;

import ru.forxy.fraud.rest.v1.velocity.VelocityData;
import ru.forxy.fraud.rest.v1.velocity.VelocityMetric;

import java.util.List;

/**
 * Black lists manipulation logic API
 */
public interface IVelocityManager {

    List<VelocityMetric> getMoreMetricsFrom(final String metricType, final String value);

    List<VelocityMetric> getMoreMetricsFrom(final String metricType, final String value, final int limit);

    List<VelocityData> getMoreDataFrom(final String metricType, final String value);

    List<VelocityData> getMoreDataFrom(final String metricType, final String value, final int limit);

    VelocityMetric getMetric(final String type, final String value);

    VelocityData getData(final String type, final String value);

    void addMetric(final VelocityMetric metric);

    void addData(final VelocityData data);

    void updateMetric(final VelocityMetric metric);

    void updateData(final VelocityData data);
}
