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
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

/**
 * Implementation class for BlackListService business logic
 */
public class VelocityManager implements IVelocityManager {

    private static final int DEFAULT_PAGE_SIZE = 30;

    private IVelocityDAO velocityDAO;
    private OperationalDataStorage operationalDataStorage;

    private final ForkJoinPool fjp = new ForkJoinPool(128);

    @Override
    public List<VelocityMetric> check(Map<String, String> metrics) {
        List<VelocityMetric> resultMetrics = new ArrayList<>();
        List<VelocityData> velocityDataToSave = new ArrayList<>();

        // getting velocity configuration from cache
        Map<String, VelocityConfig> configs = operationalDataStorage.getConfigsByMetricType();

        /*Map<VelocityMetric, List<VelocityData>> result =
                fjp.invoke(new RecursiveTask<Map<VelocityMetric, List<VelocityData>>>() {
                    private static final long serialVersionUID = 3382633942346203823L;

                    @Override
                    protected Map<VelocityMetric, List<VelocityData>> compute() {
                        return null;
                    }
                });*/

        for (Map.Entry<String, String> metric : metrics.entrySet()) {

            VelocityConfig config = configs.get(metric.getKey());

            // if incoming metric has configuration then retrieve it's related metrics configuration
            if (config != null) {
                Map<String, Set<AggregationConfig>> relatedMetrics = config.getMetricsAggregationConfig();

                if (relatedMetrics != null) {
                    // creating single partition key for metric and it's data
                    VelocityPartitionKey key = new VelocityPartitionKey(metric.getKey(), metric.getValue());


                    for (Map.Entry<String, Set<AggregationConfig>> aggregationConfigs : relatedMetrics.entrySet()) {

                        String relatedMetricType = aggregationConfigs.getKey();
                        String relatedMetricValue = metrics.get(relatedMetricType);

                        // prepare velocity data for deferred batch insert
                        VelocityData newVelocityData = new VelocityData(
                                new VelocityDataCompositeKey(key, relatedMetricType, new Date()), relatedMetricValue);
                        velocityDataToSave.add(newVelocityData);

                        if (aggregationConfigs.getValue() != null) {
                            // for each aggregation configuration calculate and update aggregation metric
                            for (AggregationConfig aggregationConfig : aggregationConfigs.getValue()) {

                                List<VelocityData> data = velocityDAO.getMetricDataForPeriod(key, relatedMetricType,
                                        aggregationConfig.getPeriod());
                                // add new data for aggregation
                                data.add(newVelocityData);

                                // apply aggregation function and add metric to result set
                                Double aggregationResult = aggregationConfig.getType().apply(data);
                                resultMetrics.add(new VelocityMetric(
                                        new VelocityMetricCompositeKey(key, relatedMetricType,
                                                aggregationConfig.getType()), aggregationResult));
                            }
                        }
                    }
                }
            }
        }

        if (resultMetrics.size() > 0) {
            velocityDAO.saveBatchOfMetrics(resultMetrics);
        }
        if (velocityDataToSave.size() > 0) {
            velocityDAO.saveBatchOfData(velocityDataToSave);
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
