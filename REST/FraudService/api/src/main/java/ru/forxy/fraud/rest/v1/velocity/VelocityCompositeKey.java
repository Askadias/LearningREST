package ru.forxy.fraud.rest.v1.velocity;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import java.io.Serializable;

/**
 * Velocity metric and data composite cluster + partition key
 */
public class VelocityCompositeKey implements Serializable {

    private static final long serialVersionUID = -6422166960856468971L;

    @EmbeddedId
    private VelocityPartitionKey id;
    @Column(name = "related_metric_type")
    private String relatedMetricType;

    public VelocityCompositeKey() {
    }

    public VelocityCompositeKey(VelocityPartitionKey id) {
        this.id = id;
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
}