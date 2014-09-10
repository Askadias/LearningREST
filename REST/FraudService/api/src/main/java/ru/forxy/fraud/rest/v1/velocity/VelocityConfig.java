package ru.forxy.fraud.rest.v1.velocity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;
import java.util.Set;

/**
 * Velocity configuration
 */
@Document(collection = "velocity_config")
public class VelocityConfig implements Serializable {

    private static final long serialVersionUID = -7728911265394463464L;

    @Id
    private String metricType;
    private Date timeToLive;
    private Map<String, Set<AggregationConfig>> metricsAggregationConfig;
    private Date createDate;
    private String createdBy;
    private Date updateDate;
    private String updatedBy;

    public VelocityConfig() {
    }

    public VelocityConfig(String metricType, String updatedBy, String createdBy) {
        this.metricType = metricType;
        this.updatedBy = updatedBy;
        this.createdBy = createdBy;
    }

    public String getMetricType() {
        return metricType;
    }

    public void setMetricType(String metricType) {
        this.metricType = metricType;
    }

    public Date getTimeToLive() {
        return timeToLive;
    }

    public void setTimeToLive(Date timeToLive) {
        this.timeToLive = timeToLive;
    }

    public Map<String, Set<AggregationConfig>> getMetricsAggregationConfig() {
        return metricsAggregationConfig;
    }

    public void setMetricsAggregationConfig(Map<String, Set<AggregationConfig>> metricsAggregationConfig) {
        this.metricsAggregationConfig = metricsAggregationConfig;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }
}
