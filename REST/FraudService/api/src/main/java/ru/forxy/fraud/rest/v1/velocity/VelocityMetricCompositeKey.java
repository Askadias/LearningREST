package ru.forxy.fraud.rest.v1.velocity;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import java.io.Serializable;

/**
 * Composite cluster+partition key for velocity
 */
public class VelocityMetricCompositeKey implements Serializable {

    private static final long serialVersionUID = -3245065035725879278L;

    @EmbeddedId
    private VelocityPartitionKey id;
    @Column(name = "related_metric_type")
    private String relatedMetricType;
    @Column(name = "aggregation_type")
    private AggregationType aggregationType;

    public VelocityMetricCompositeKey() {
    }

    public VelocityMetricCompositeKey(final VelocityPartitionKey id, final String relatedMetricType,
                                      final AggregationType aggregationType) {
        this.id = id;
        this.relatedMetricType = relatedMetricType;
        this.aggregationType = aggregationType;
    }

    public VelocityPartitionKey getId() {
        return id;
    }

    public void setId(VelocityPartitionKey id) {
        this.id = id;
    }

    public String getRelatedMetricType() {
        return relatedMetricType;
    }

    public void setRelatedMetricType(String relatedMetricType) {
        this.relatedMetricType = relatedMetricType;
    }

    public AggregationType getAggregationType() {
        return aggregationType;
    }

    public void setAggregationType(AggregationType aggregationType) {
        this.aggregationType = aggregationType;
    }
}
