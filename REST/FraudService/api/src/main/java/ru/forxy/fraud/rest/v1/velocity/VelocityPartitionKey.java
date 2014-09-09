package ru.forxy.fraud.rest.v1.velocity;

import java.io.Serializable;

/**
 * Velocity metrics cassandra partition key
 */
public class VelocityPartitionKey implements Serializable {

    private static final long serialVersionUID = -7509974707407062638L;

    private String metricType;
    private String value;

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
