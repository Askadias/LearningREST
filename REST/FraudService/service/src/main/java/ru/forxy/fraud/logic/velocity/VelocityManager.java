package ru.forxy.fraud.logic.velocity;

import ru.forxy.fraud.db.dao.IVelocityDAO;
import ru.forxy.fraud.rest.v1.velocity.VelocityData;
import ru.forxy.fraud.rest.v1.velocity.VelocityMetric;
import ru.forxy.fraud.rest.v1.velocity.VelocityPartitionKey;

import java.util.List;

/**
 * Implementation class for BlackListService business logic
 */
public class VelocityManager implements IVelocityManager {

    private static final int DEFAULT_PAGE_SIZE = 10;

    private IVelocityDAO velocityDAO;

    @Override
    public List<VelocityMetric> getMoreMetricsFrom(String metricType, String value) {
        return getMoreMetricsFrom(metricType, value, DEFAULT_PAGE_SIZE);
    }

    @Override
    public List<VelocityMetric> getMoreMetricsFrom(String metricType, String value, int limit) {
        return velocityDAO.getMoreMetrics(metricType, value, limit);
    }

    @Override
    public List<VelocityData> getMoreDataFrom(String metricType, String value) {
        return getMoreDataFrom(metricType, value, DEFAULT_PAGE_SIZE);
    }

    @Override
    public List<VelocityData> getMoreDataFrom(String metricType, String value, int limit) {
        return velocityDAO.getMoreData(metricType, value, limit);
    }

    @Override
    public VelocityMetric getMetric(String type, String value) {
        return velocityDAO.getMetric(new VelocityPartitionKey(type, value));
    }

    @Override
    public VelocityData getData(String type, String value) {
        return velocityDAO.getData(new VelocityPartitionKey(type, value));
    }

    @Override
    public void addMetric(VelocityMetric metric) {
        velocityDAO.saveMetric(metric);
    }

    @Override
    public void addData(VelocityData data) {
        velocityDAO.saveData(data);
    }

    @Override
    public void updateMetric(VelocityMetric metric) {
        velocityDAO.saveMetric(metric);
    }

    @Override
    public void updateData(VelocityData data) {
        velocityDAO.saveData(data);
    }

    public void setVelocityDAO(IVelocityDAO velocityDAO) {
        this.velocityDAO = velocityDAO;
    }
}
