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
    private VelocityMetricCompositeKey key;
    @Column(name = "aggregated_value")
    private Double aggregatedValue;

    public VelocityMetric() {
    }

    public VelocityMetric(final VelocityMetricCompositeKey key, final Double aggregatedValue) {
        this.key = key;
        this.aggregatedValue = aggregatedValue;
    }

    public VelocityMetricCompositeKey getKey() {
        return key;
    }

    public void setKey(VelocityMetricCompositeKey key) {
        this.key = key;
    }

    public Double getAggregatedValue() {
        return aggregatedValue;
    }

    public void setAggregatedValue(Double aggregatedValue) {
        this.aggregatedValue = aggregatedValue;
    }
}
