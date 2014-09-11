package ru.forxy.fraud.logic.velocity;

import ru.forxy.fraud.db.dao.IVelocityDAO;
import ru.forxy.fraud.rest.v1.velocity.AggregationType;
import ru.forxy.fraud.rest.v1.velocity.VelocityData;
import ru.forxy.fraud.rest.v1.velocity.VelocityDataCompositeKey;
import ru.forxy.fraud.rest.v1.velocity.VelocityMetric;
import ru.forxy.fraud.rest.v1.velocity.VelocityMetricCompositeKey;
import ru.forxy.fraud.rest.v1.velocity.VelocityPartitionKey;

import java.util.Date;
import java.util.List;

/**
 * Implementation class for BlackListService business logic
 */
public class VelocityManager implements IVelocityManager {

    private static final int DEFAULT_PAGE_SIZE = 10;

    private IVelocityDAO velocityDAO;

    @Override
    public List<VelocityMetric> getMoreMetricsFrom(final String metricType, final String value) {
        return getMoreMetricsFrom(metricType, value, DEFAULT_PAGE_SIZE);
    }

    @Override
    public List<VelocityMetric> getMoreMetricsFrom(final String metricType, final String value, final int limit) {
        return velocityDAO.getMoreMetrics(metricType, value, limit);
    }

    @Override
    public List<VelocityMetric> getMetrics(final String metricType, final String metricValue) {
        return velocityDAO.getMetrics(new VelocityPartitionKey(metricType, metricValue));
    }

    @Override
    public VelocityMetric getMetric(final String metricType, final String metricValue, final String relatedMetricType,
                                    final AggregationType aggregationType) {
        return velocityDAO.getMetric(new VelocityMetricCompositeKey(
                new VelocityPartitionKey(metricType, metricValue), relatedMetricType, aggregationType));
    }

    @Override
    public List<VelocityData> getMoreDataFrom(final String metricType, final String value) {
        return getMoreDataFrom(metricType, value, DEFAULT_PAGE_SIZE);
    }

    @Override
    public List<VelocityData> getMoreDataFrom(final String metricType, final String value, final int limit) {
        return velocityDAO.getMoreData(metricType, value, limit);
    }

    @Override
    public List<VelocityData> getDataList(final String metricType, final String metricValue) {
        return velocityDAO.getDataList(new VelocityPartitionKey(metricType, metricValue));
    }

    @Override
    public VelocityData getData(final String metricType, final String metricValue, final String relatedMetricType,
                                final Date createDate) {
        return velocityDAO.getData(new VelocityDataCompositeKey(
                new VelocityPartitionKey(metricType, metricValue), relatedMetricType, createDate));
    }

    @Override
    public void addMetric(final VelocityMetric metric) {
        velocityDAO.saveMetric(metric);
    }

    @Override
    public void addData(final VelocityData data) {
        velocityDAO.saveData(data);
    }

    @Override
    public void updateMetric(final VelocityMetric metric) {
        velocityDAO.saveMetric(metric);
    }

    @Override
    public void updateData(final VelocityData data) {
        velocityDAO.saveData(data);
    }

    public void setVelocityDAO(final IVelocityDAO velocityDAO) {
        this.velocityDAO = velocityDAO;
    }
}
