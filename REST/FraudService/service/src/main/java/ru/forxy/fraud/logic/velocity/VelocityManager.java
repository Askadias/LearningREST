package ru.forxy.fraud.logic.velocity;

import ru.forxy.fraud.db.dao.IVelocityDAO;
import ru.forxy.fraud.rest.v1.velocity.AggregationConfig;
import ru.forxy.fraud.rest.v1.velocity.AggregationType;
import ru.forxy.fraud.rest.v1.velocity.VelocityConfig;
import ru.forxy.fraud.rest.v1.velocity.VelocityData;
import ru.forxy.fraud.rest.v1.velocity.VelocityDataCompositeKey;
import ru.forxy.fraud.rest.v1.velocity.VelocityMetric;
import ru.forxy.fraud.rest.v1.velocity.VelocityMetricCompositeKey;
import ru.forxy.fraud.rest.v1.velocity.VelocityPartitionKey;
import ru.forxy.fraud.util.OperationalDataStorage;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Implementation class for BlackListService business logic
 */
public class VelocityManager implements IVelocityManager {

    private static final int DEFAULT_PAGE_SIZE = 30;

    private IVelocityDAO velocityDAO;
    private OperationalDataStorage operationalDataStorage;

    @Override
    public List<VelocityMetric> check(Map<String, String> metrics) {
        List<VelocityMetric> resultMetrics = new ArrayList<>();


        Map<String, VelocityConfig> configs = operationalDataStorage.getConfigsByMetricType();

        for (Map.Entry<String, String> metric : metrics.entrySet()) {

            VelocityConfig config = configs.get(metric.getKey());

            if (config != null) {
                Map<String, Set<AggregationConfig>> relatedMetrics = config.getMetricsAggregationConfig();

                if (relatedMetrics != null) {

                    for (Map.Entry<String, Set<AggregationConfig>> aggConfigs : relatedMetrics.entrySet()) {

                        String relatedMetricType = aggConfigs.getKey();
                        String relatedMetricValue = metrics.get(relatedMetricType);
                        VelocityPartitionKey key = new VelocityPartitionKey(metric.getKey(), metric.getValue());

                        velocityDAO.saveData(new VelocityData(
                                new VelocityDataCompositeKey(key, relatedMetricType, new Date()), relatedMetricValue));

                        if (aggConfigs.getValue() != null) {
                            for (AggregationConfig aggregationConfig : aggConfigs.getValue()) {

                                List<VelocityData> data = velocityDAO.getMetricDataForPeriod(key, relatedMetricType,
                                        aggregationConfig.getPeriod());

                                Double aggregationResult = aggregationConfig.getType().apply(data);
                                velocityDAO.saveMetric(new VelocityMetric(
                                        new VelocityMetricCompositeKey(key, relatedMetricType,
                                                aggregationConfig.getType()), aggregationResult));
                            }
                        }
                    }
                }
            }
        }

        for (Map.Entry<String, String> metric : metrics.entrySet()) {
            resultMetrics.addAll(velocityDAO.getMetrics(new VelocityPartitionKey(metric.getKey(), metric.getValue())));
        }
        // return
        return resultMetrics;
    }

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
    public List<VelocityData> getMoreDataFrom(final String metricType, final String value,
                                              final String relatedMetricType, final Date createDate) {
        return getMoreDataFrom(metricType, value, relatedMetricType, createDate, DEFAULT_PAGE_SIZE);
    }

    @Override
    public List<VelocityData> getMoreDataFrom(final String metricType, final String value,
                                              final String relatedMetricType, final Date createDate, final int limit) {
        return velocityDAO.getMoreData(metricType, value, relatedMetricType, createDate, limit);
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

    public void setOperationalDataStorage(OperationalDataStorage operationalDataStorage) {
        this.operationalDataStorage = operationalDataStorage;
    }
}
