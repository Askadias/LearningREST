package ru.forxy.fraud.rest.v1.velocity

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

/**
 * Velocity configuration
 */
@Document(collection = "velocity_config")
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
