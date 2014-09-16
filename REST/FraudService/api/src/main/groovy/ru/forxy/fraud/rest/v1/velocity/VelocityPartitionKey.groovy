package ru.forxy.fraud.rest.v1.velocity

import javax.persistence.Column

/**
 * Velocity metrics cassandra partition key
 */
class VelocityPartitionKey implements Serializable {
    @Column(name = "metric_type")
    String metricType;
    @Column(name = "metric_value")
    String metricValue;
}
