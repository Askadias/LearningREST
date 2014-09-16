package ru.forxy.fraud.rest.v1.velocity

import javax.persistence.Column
import javax.persistence.EmbeddedId

/**
 * Composite cluster+partition key for velocity
 */
class VelocityMetricCompositeKey implements Serializable {
    @EmbeddedId
    VelocityPartitionKey id;
    @Column(name = "related_metric_type")
    String relatedMetricType;
    @Column(name = "aggregation_type")
    AggregationType aggregationType;
}
