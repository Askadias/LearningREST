package ru.forxy.fraud.logic.velocity.concurrency;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.forxy.common.exceptions.ServiceException;
import ru.forxy.fraud.db.dao.IVelocityDAO;
import ru.forxy.fraud.exceptions.FraudServiceEventLogId;
import ru.forxy.fraud.rest.v1.velocity.AggregationConfig;
import ru.forxy.fraud.rest.v1.velocity.VelocityConfig;
import ru.forxy.fraud.rest.v1.velocity.VelocityData;
import ru.forxy.fraud.rest.v1.velocity.VelocityDataCompositeKey;
import ru.forxy.fraud.rest.v1.velocity.VelocityMetric;
import ru.forxy.fraud.rest.v1.velocity.VelocityPartitionKey;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.RecursiveTask;

/**
 * Velocity concurrent update task
 */
public class RelatedMetricsComputationTask extends RecursiveTask<Map<VelocityPartitionKey, Map<VelocityData, List<VelocityMetric>>>> {

    private static final long serialVersionUID = -6196833540231171108L;

    private static final Logger LOGGER = LoggerFactory.getLogger(RelatedMetricsComputationTask.class);

    private final VelocityConfig config;
    private final String metricType;
    private final String metricValue;
    private final Map<String, String> metrics;
    private final IVelocityDAO velocityDAO;

    public RelatedMetricsComputationTask(final VelocityConfig config,
                                         final String metricType,
                                         final String metricValue,
                                         final Map<String, String> metrics,
                                         final IVelocityDAO velocityDAO) {
        this.config = config;
        this.metricType = metricType;
        this.metricValue = metricValue;
        this.metrics = metrics;
        this.velocityDAO = velocityDAO;
    }


    @Override
    protected Map<VelocityPartitionKey, Map<VelocityData, List<VelocityMetric>>> compute() {
        Map<VelocityPartitionKey, Map<VelocityData, List<VelocityMetric>>> result = new HashMap<>();
        Map<String, Set<AggregationConfig>> relatedMetrics = config.getMetricsAggregationConfig();
        if (relatedMetrics != null) {
            // creating single partition key for metric and it's data
            final VelocityPartitionKey key = new VelocityPartitionKey(metricType, metricValue);
            Map<VelocityData, List<VelocityMetric>> metricResults = new HashMap<>();
            result.put(key, metricResults);

            List<VelocityMetricComputationTask> velocityMetricComputationTasks = new ArrayList<>();

            for (final Map.Entry<String, Set<AggregationConfig>> aggregationConfigs : relatedMetrics.entrySet()) {
                final String relatedMetricType = aggregationConfigs.getKey();
                final String relatedMetricValue = metrics.get(relatedMetricType);
                final VelocityData newVelocityData = new VelocityData(
                        new VelocityDataCompositeKey(key, relatedMetricType, new Date()), relatedMetricValue);
                velocityMetricComputationTasks.add(
                        new VelocityMetricComputationTask(
                                key,
                                aggregationConfigs.getValue(),
                                velocityDAO,
                                newVelocityData,
                                relatedMetricType));
            }
            invokeAll(velocityMetricComputationTasks);
            for (VelocityMetricComputationTask velocityMetricComputationTask : velocityMetricComputationTasks) {
                try {
                    metricResults.put(velocityMetricComputationTask.getNewVelocityData(), velocityMetricComputationTask.get());
                } catch (InterruptedException | ExecutionException e) {
                    new ServiceException(e,
                            FraudServiceEventLogId.UnexpectedErrorDuringVelocityComputation,
                            key.getMetricValue()).log(LOGGER);
                }
            }
        }
        return result;
    }

    public String getMetricType() {
        return metricType;
    }
}
