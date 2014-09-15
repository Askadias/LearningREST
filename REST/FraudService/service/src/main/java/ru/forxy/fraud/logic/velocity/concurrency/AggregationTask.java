package ru.forxy.fraud.logic.velocity.concurrency;

import ru.forxy.fraud.rest.v1.velocity.AggregationType;
import ru.forxy.fraud.rest.v1.velocity.VelocityData;
import ru.forxy.fraud.rest.v1.velocity.VelocityMetric;
import ru.forxy.fraud.rest.v1.velocity.VelocityMetricCompositeKey;
import ru.forxy.fraud.rest.v1.velocity.VelocityPartitionKey;

import java.util.List;
import java.util.concurrent.RecursiveTask;

/**
 * Aggregation task to be executed in parallel thread
 */
public class AggregationTask extends RecursiveTask<VelocityMetric> {

    private static final long serialVersionUID = -332286418841242183L;

    private final VelocityPartitionKey key;
    final String relatedMetricType;
    final AggregationType aggregationType;
    final List<VelocityData> dataList;

    public AggregationTask(final VelocityPartitionKey key,
                           final String relatedMetricType,
                           final List<VelocityData> dataList,
                           final AggregationType aggregationType) {
        this.key = key;
        this.relatedMetricType = relatedMetricType;
        this.dataList = dataList;
        this.aggregationType = aggregationType;
    }

    @Override
    protected VelocityMetric compute() {
        return new VelocityMetric(new VelocityMetricCompositeKey(key, relatedMetricType, aggregationType),
                aggregationType.apply(dataList));
    }
}
