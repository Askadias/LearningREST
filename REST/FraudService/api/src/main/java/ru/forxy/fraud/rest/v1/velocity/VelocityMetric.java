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
    private CompositeKey key;
    @Column(name = "aggregated_value")
    private Double aggregatedValue;

    public CompositeKey getKey() {
        return key;
    }

    public void setKey(CompositeKey key) {
        this.key = key;
    }

    public Double getAggregatedValue() {
        return aggregatedValue;
    }

    public void setAggregatedValue(Double aggregatedValue) {
        this.aggregatedValue = aggregatedValue;
    }

    public class CompositeKey implements Serializable {

        private static final long serialVersionUID = -3245065035725879278L;

        @EmbeddedId
        private VelocityPartitionKey id;
        @Column(name = "related_metric_type")
        private String relatedMetricType;
        @Column(name = "aggregation_type")
        private AggregationType aggregationType;

        public CompositeKey() {
        }

        public CompositeKey(VelocityPartitionKey id) {
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

        public AggregationType getAggregationType() {
            return aggregationType;
        }

        public void setAggregationType(AggregationType aggregationType) {
            this.aggregationType = aggregationType;
        }
    }
}
