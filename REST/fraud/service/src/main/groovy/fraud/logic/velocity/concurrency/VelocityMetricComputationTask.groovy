package fraud.logic.velocity.concurrency

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import common.exceptions.ServiceException
import fraud.db.dao.IVelocityDAO
import fraud.exceptions.FraudServiceEventLogId
import fraud.rest.v1.velocity.AggregationConfig
import fraud.rest.v1.velocity.VelocityData
import fraud.rest.v1.velocity.VelocityMetric
import fraud.rest.v1.velocity.VelocityPartitionKey

import java.util.concurrent.ExecutionException
import java.util.concurrent.RecursiveTask

/**
 * Calculates list of metrics for the configured period
 */
class VelocityMetricComputationTask extends RecursiveTask<List<VelocityMetric>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(VelocityMetricComputationTask.class)

    VelocityPartitionKey key
    Set<AggregationConfig> aggregationConfigs
    IVelocityDAO velocityDAO
    VelocityData newVelocityData
    String relatedMetricType

    @Override
    protected List<VelocityMetric> compute() {
        List<VelocityMetric> metrics = []
        List<AggregationTask> aggregationTasks = new ArrayList<>(aggregationConfigs.size())
        aggregationConfigs.each {
            List<VelocityData> data = velocityDAO.getMetricDataForPeriod(key, relatedMetricType, it.period)

            if (newVelocityData.relatedMetricValue) {
                // add new incoming data for aggregation:
                data << newVelocityData
            }

            // apply aggregation function:
            aggregationTasks.add(new AggregationTask(
                    key: key,
                    relatedMetricType: relatedMetricType,
                    dataList: data,
                    aggregationType: it.getType()))
        }

        // run concurrently:
        invokeAll(aggregationTasks)
        aggregationTasks.each {
            try {
                metrics.add(it.get())
            } catch (InterruptedException | ExecutionException e) {
                new ServiceException(e,
                        FraudServiceEventLogId.UnexpectedErrorDuringVelocityComputation,
                        key.getMetricValue()).log(LOGGER)
            }
        }
        metrics
    }
}
