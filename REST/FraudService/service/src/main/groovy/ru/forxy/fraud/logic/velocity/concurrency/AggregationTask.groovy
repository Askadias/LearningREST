package ru.forxy.fraud.logic.velocity.concurrency

import ru.forxy.fraud.rest.v1.velocity.*

import java.util.concurrent.RecursiveTask

/**
 * Aggregation task to be executed in parallel thread
 */
class AggregationTask extends RecursiveTask<VelocityMetric> {
    VelocityPartitionKey key
    String relatedMetricType
    AggregationType aggregationType
    List<VelocityData> dataList

    @Override
    protected VelocityMetric compute() {
        new VelocityMetric(
                key: new VelocityMetricCompositeKey(
                        id: key,
                        relatedMetricType: relatedMetricType,
                        aggregationType: aggregationType),
                aggregatedValue: aggregationType.apply(dataList))
    }
}
