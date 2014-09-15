package ru.forxy.fraud.logic.velocity.concurrency;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.forxy.common.exceptions.ServiceException;
import ru.forxy.fraud.db.dao.IVelocityDAO;
import ru.forxy.fraud.exceptions.FraudServiceEventLogId;
import ru.forxy.fraud.rest.v1.velocity.AggregationConfig;
import ru.forxy.fraud.rest.v1.velocity.VelocityData;
import ru.forxy.fraud.rest.v1.velocity.VelocityMetric;
import ru.forxy.fraud.rest.v1.velocity.VelocityPartitionKey;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.RecursiveTask;

/**
 * Calculates list of metrics for the configured period
 */
public class VelocityMetricComputationTask extends RecursiveTask<List<VelocityMetric>> {

    private static final long serialVersionUID = 4768345379121200903L;

    private static final Logger LOGGER = LoggerFactory.getLogger(VelocityMetricComputationTask.class);

    private final VelocityPartitionKey key;
    private final Set<AggregationConfig> aggregationConfigs;
    private final IVelocityDAO velocityDAO;
    private final VelocityData newVelocityData;
    private final String relatedMetricType;

    public VelocityMetricComputationTask(final VelocityPartitionKey key,
                                         final Set<AggregationConfig> aggregationConfigs,
                                         final IVelocityDAO velocityDAO,
                                         final VelocityData newVelocityData,
                                         final String relatedMetricType) {
        this.key = key;
        this.aggregationConfigs = aggregationConfigs;
        this.velocityDAO = velocityDAO;
        this.newVelocityData = newVelocityData;
        this.relatedMetricType = relatedMetricType;
    }

    @Override
    protected List<VelocityMetric> compute() {
        List<VelocityMetric> metrics = new ArrayList<>();
        List<AggregationTask> aggregationTasks = new ArrayList<>(aggregationConfigs.size());
        for (AggregationConfig aggregationConfig : aggregationConfigs) {
            List<VelocityData> data = velocityDAO.getMetricDataForPeriod(key, relatedMetricType,
                    aggregationConfig.getPeriod());
            // add new incoming data for aggregation:
            data.add(newVelocityData);

            // apply aggregation function:
            aggregationTasks.add(new AggregationTask(key, relatedMetricType, data, aggregationConfig.getType()));
        }

        // run concurrently:
        invokeAll(aggregationTasks);
        for (AggregationTask task : aggregationTasks) {
            try {
                metrics.add(task.get());
            } catch (InterruptedException | ExecutionException e) {
                new ServiceException(e,
                        FraudServiceEventLogId.UnexpectedErrorDuringVelocityComputation,
                        key.getMetricValue()).log(LOGGER);
            }
        }
        return metrics;
    }

    public VelocityData getNewVelocityData() {
        return newVelocityData;
    }
}
