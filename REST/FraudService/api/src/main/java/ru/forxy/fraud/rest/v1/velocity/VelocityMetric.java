package ru.forxy.fraud.rest.v1.velocity;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Velocity metric row definition
 */
@Table(name = "velocity")
public class VelocityMetric implements Serializable {

    private static final long serialVersionUID = -7204065035725819278L;

    @EmbeddedId
    private VelocityPartitionKey key;
    @Column(name = "metric_type")
    private String metricType;
    @Column(name = "aggregation_type")
    private AggregationType aggregationType;
    private Double value;

    public VelocityPartitionKey getKey() {
        return key;
    }

    public void setKey(VelocityPartitionKey key) {
        this.key = key;
    }

    public String getMetricType() {
        return metricType;
    }

    public void setMetricType(String metricType) {
        this.metricType = metricType;
    }

    public AggregationType getAggregationType() {
        return aggregationType;
    }

    public void setAggregationType(AggregationType aggregationType) {
        this.aggregationType = aggregationType;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }
}
