package velocity

import grails.rest.Resource

@Resource(uri = "/configs", formats = ['json'])
class VelocityConfig {

    String metricType;
    Long timeToLive;
    Map<String, Set<Aggregation>> metricsAggregationConfig;
    Date createDate;
    String createdBy;
    Date updateDate;
    String updatedBy;

    static constraints = {
        metricType blank: false
        timeToLive nullable: true
        metricsAggregationConfig nullable: true
        createdBy nullable: true
        updatedBy nullable: true
    }
    static mapWith = "mongo"
}
