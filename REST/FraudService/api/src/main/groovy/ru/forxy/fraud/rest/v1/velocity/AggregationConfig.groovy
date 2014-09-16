package ru.forxy.fraud.rest.v1.velocity
/**
 * Related metrics aggregation configuration
 */
class AggregationConfig implements Serializable {
    AggregationType type;
    Long period;
}
