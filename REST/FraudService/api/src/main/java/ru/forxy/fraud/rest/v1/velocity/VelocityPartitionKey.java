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
    private String value;

    public VelocityPartitionKey() {
    }

    public VelocityPartitionKey(String metricType, String value) {
        this.metricType = metricType;
        this.value = value;
    }

    public String getMetricType() {
        return metricType;
    }

    public void setMetricType(String metricType) {
        this.metricType = metricType;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
