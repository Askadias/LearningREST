package ru.forxy.fraud.rest.v1.velocity

import javax.persistence.Column
import javax.persistence.EmbeddedId
import javax.persistence.Table

/**
 * Contains raw data set for different velocity metrics
 */
@Table(name = "velocity_data")
class VelocityData implements Serializable {
    @EmbeddedId
    VelocityDataCompositeKey key;
    @Column(name = "related_metric_value")
    String relatedMetricValue;
}
