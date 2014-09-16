package ru.forxy.fraud.logic.velocity.concurrency

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import ru.forxy.common.exceptions.ServiceException
import ru.forxy.fraud.db.dao.IVelocityDAO
import ru.forxy.fraud.exceptions.FraudServiceEventLogId
import ru.forxy.fraud.rest.v1.velocity.*

import java.util.concurrent.ExecutionException
import java.util.concurrent.RecursiveTask

/**
 * Velocity concurrent update task
 */
class RelatedMetricsComputationTask extends RecursiveTask<Map<VelocityPartitionKey, Map<VelocityData, List<VelocityMetric>>>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(RelatedMetricsComputationTask.class)

    VelocityConfig config
    String metricType
    String metricValue
    Map<String, String> metrics
    IVelocityDAO velocityDAO

    @Override
    protected Map<VelocityPartitionKey, Map<VelocityData, List<VelocityMetric>>> compute() {
        Map<VelocityPartitionKey, Map<VelocityData, List<VelocityMetric>>> result = new HashMap<>()
        Map<String, Set<AggregationConfig>> relatedMetrics = config.metricsAggregationConfig
        if (relatedMetrics != null) {
            // creating single partition key for metric and it's data
            final VelocityPartitionKey key = new VelocityPartitionKey(metricType: metricType, metricValue: metricValue)
            Map<VelocityData, List<VelocityMetric>> metricResults = new HashMap<>()
            result.put(key, metricResults)

            List<VelocityMetricComputationTask> velocityMetricComputationTasks = []

            for (final Map.Entry<String, Set<AggregationConfig>> aggregationConfigs : relatedMetrics.entrySet()) {
                final String relatedMetricType = aggregationConfigs.getKey()
                final String relatedMetricValue = metrics.get(relatedMetricType)
                final VelocityData newVelocityData = new VelocityData(
                        key: new VelocityDataCompositeKey(
                                id: key,
                                relatedMetricType: relatedMetricType,
                                createDate: new Date()),
                        relatedMetricValue: relatedMetricValue)
                velocityMetricComputationTasks.add(
                        new VelocityMetricComputationTask(
                                key: key,
                                aggregationConfigs: aggregationConfigs.getValue(),
                                velocityDAO: velocityDAO,
                                newVelocityData: newVelocityData,
                                relatedMetricType: relatedMetricType))
            }
            invokeAll(velocityMetricComputationTasks)
            for (VelocityMetricComputationTask velocityMetricComputationTask : velocityMetricComputationTasks) {
                try {
                    metricResults.put(velocityMetricComputationTask.getNewVelocityData(), velocityMetricComputationTask.get())
                } catch (InterruptedException | ExecutionException e) {
                    new ServiceException(e,
                            FraudServiceEventLogId.UnexpectedErrorDuringVelocityComputation,
                            key.getMetricValue()).log(LOGGER)
                }
            }
        }
        result
    }
}
