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
    private VelocityCompositeKey key;
    @Column(name = "aggregation_type")
    private AggregationType aggregationType;
    @Column(name = "aggregated_value")
    private Double aggregatedValue;

    public VelocityCompositeKey getKey() {
        return key;
    }

    public void setKey(VelocityCompositeKey key) {
        this.key = key;
    }

    public AggregationType getAggregationType() {
        return aggregationType;
    }

    public void setAggregationType(AggregationType aggregationType) {
        this.aggregationType = aggregationType;
    }

    public Double getAggregatedValue() {
        return aggregatedValue;
    }

    public void setAggregatedValue(Double aggregatedValue) {
        this.aggregatedValue = aggregatedValue;
    }
}
