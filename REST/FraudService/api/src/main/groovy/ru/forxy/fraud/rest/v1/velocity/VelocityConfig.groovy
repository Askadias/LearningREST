package ru.forxy.fraud.rest.v1.velocity

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

/**
 * Velocity configuration
 */
@Document(collection = "velocity_config")
@ToString
@EqualsAndHashCode
class VelocityConfig implements Serializable {
    @Id
    String metricType;
    Long timeToLive;
    Map<String, Set<AggregationConfig>> metricsAggregationConfig;
    Date createDate;
    String createdBy;
    Date updateDate;
    String updatedBy;
}
