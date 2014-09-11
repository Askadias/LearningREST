package ru.forxy.fraud.rest.v1.velocity;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * Contains raw data set for different velocity metrics
 */
@Table(name = "velocity_data")
public class VelocityData implements Serializable {

    private static final long serialVersionUID = 6850650473701192980L;

    @EmbeddedId
    private VelocityDataCompositeKey key;
    @Column(name = "related_metric_value")
    private String relatedMetricValue;

    public VelocityDataCompositeKey getKey() {
        return key;
    }

    public void setKey(VelocityDataCompositeKey key) {
        this.key = key;
    }

    public String getRelatedMetricValue() {
        return relatedMetricValue;
    }

    public void setRelatedMetricValue(String relatedMetricValue) {
        this.relatedMetricValue = relatedMetricValue;
    }
}
