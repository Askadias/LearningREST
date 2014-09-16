package ru.forxy.fraud.rest.v1.velocity

import javax.persistence.Column
import javax.persistence.EmbeddedId
import javax.persistence.Table

/**
 * Velocity metric row definition
 */
@Table(name = "velocity")
class VelocityMetric implements Serializable {
    @EmbeddedId
    VelocityMetricCompositeKey key;
    @Column(name = "aggregated_value")
    Double aggregatedValue;
}
