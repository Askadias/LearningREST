package ru.forxy.fraud.logic.velocity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.forxy.common.exceptions.ServiceException;
import ru.forxy.fraud.db.dao.IVelocityDAO;
import ru.forxy.fraud.exceptions.FraudServiceEventLogId;
import ru.forxy.fraud.logic.velocity.concurrency.RelatedMetricsComputationTask;
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
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;

/**
 * Implementation class for BlackListService business logic
 */
public class VelocityManager implements IVelocityManager {

    private static final int DEFAULT_PAGE_SIZE = 30;

    private static final Logger LOGGER = LoggerFactory.getLogger(VelocityManager.class);

    private IVelocityDAO velocityDAO;
    private OperationalDataStorage operationalDataStorage;

    private final ForkJoinPool fjp = new ForkJoinPool(32);

    @Override
    public List<VelocityMetric> check(final Map<String, String> metrics) {
        List<VelocityMetric> resultMetrics = new ArrayList<>();
        List<VelocityData> velocityDataToSave = new ArrayList<>();

        // getting velocity configuration from cache
        final Map<String, VelocityConfig> configs = operationalDataStorage.getConfigsByMetricType();

        List<RelatedMetricsComputationTask> velocityDataUpdateTasks = new ArrayList<>();
        for (Map.Entry<String, String> metric : metrics.entrySet()) {

            final VelocityConfig config = configs.get(metric.getKey());

            // if incoming metric has configuration then retrieve it's related metrics configuration
            if (config != null) {
                RelatedMetricsComputationTask dataUpdateTask = new RelatedMetricsComputationTask(config,
                        metric.getKey(),
                        metric.getValue(),
                        metrics,
                        velocityDAO);
                velocityDataUpdateTasks.add(dataUpdateTask);
                fjp.submit(dataUpdateTask);
            }
        }
        for (RelatedMetricsComputationTask dataUpdateTask : velocityDataUpdateTasks) {
            try {
                for (Map.Entry<VelocityPartitionKey, Map<VelocityData, List<VelocityMetric>>> metricResults :
                        dataUpdateTask.get().entrySet()) {
                    for (Map.Entry<VelocityData, List<VelocityMetric>> metric : metricResults.getValue().entrySet()) {
                        velocityDataToSave.add(metric.getKey());
                        resultMetrics.addAll(metric.getValue());
                    }
                }
            } catch (InterruptedException | ExecutionException e) {
                new ServiceException(e,
                        FraudServiceEventLogId.UnexpectedErrorDuringVelocityComputation,
                        dataUpdateTask.getMetricType()).log(LOGGER);
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
