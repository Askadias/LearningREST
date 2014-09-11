package ru.forxy.fraud.rest.v1.velocity;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import java.io.Serializable;
import java.util.Date;

/**
 * Composite cluster+partition key for velocity_data
 */
public class VelocityDataCompositeKey implements Serializable {

    private static final long serialVersionUID = -6422166960856468971L;

    @EmbeddedId
    private VelocityPartitionKey id;
    @Column(name = "related_metric_type")
    private String relatedMetricType;
    @Column(name = "create_date")
    private Date createDate;

    public VelocityDataCompositeKey() {
    }

    public VelocityDataCompositeKey(final VelocityPartitionKey id, final String relatedMetricType, final Date createDate) {
        this.id = id;
        this.relatedMetricType = relatedMetricType;
        this.createDate = createDate;
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
