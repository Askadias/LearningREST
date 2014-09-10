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
    private CompositeKey key;
    @Column(name = "related_metric_value")
    private String relatedMetricValue;

    public CompositeKey getKey() {
        return key;
    }

    public void setKey(CompositeKey key) {
        this.key = key;
    }

    public String getRelatedMetricValue() {
        return relatedMetricValue;
    }

    public void setRelatedMetricValue(String relatedMetricValue) {
        this.relatedMetricValue = relatedMetricValue;
    }

    public class CompositeKey implements Serializable {

        private static final long serialVersionUID = -6422166960856468971L;

        @EmbeddedId
        private VelocityPartitionKey id;
        @Column(name = "related_metric_type")
        private String relatedMetricType;
        @Column(name = "create_date")
        private Date createDate;

        public CompositeKey() {
        }

        public CompositeKey(VelocityPartitionKey id) {
            this.id = id;
        }

        public VelocityPartitionKey getId() {
            return id;
        }

        public void setId(VelocityPartitionKey id) {
            this.id = id;
        }

        public String getRelatedMetricType() {
            return relatedMetricType;
        }

        public void setRelatedMetricType(String relatedMetricType) {
            this.relatedMetricType = relatedMetricType;
        }

        public Date getCreateDate() {
            return createDate;
        }

        public void setCreateDate(Date createDate) {
            this.createDate = createDate;
        }
    }
}
