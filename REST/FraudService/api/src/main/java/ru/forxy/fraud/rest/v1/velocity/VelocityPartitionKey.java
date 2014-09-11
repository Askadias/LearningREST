package ru.forxy.fraud.rest.v1.velocity;

import javax.persistence.Column;
import java.io.Serializable;

/**
 * Velocity metrics cassandra partition key
 */
public class VelocityPartitionKey implements Serializable {

    private static final long serialVersionUID = -7509974707407062638L;

    @Column(name = "metric_type")
    private String metricType;
    @Column(name = "metric_value")
    private String metricValue;

    public VelocityPartitionKey() {
    }

    public VelocityPartitionKey(String metricType, String metricValue) {
        this.metricType = metricType;
        this.metricValue = metricValue;
    }

    public String getMetricType() {
        return metricType;
    }

    public void setMetricType(String metricType) {
        this.metricType = metricType;
    }

    public String getMetricValue() {
        return metricValue;
    }

    public void setMetricValue(String metricValue) {
        this.metricValue = metricValue;
    }
}
