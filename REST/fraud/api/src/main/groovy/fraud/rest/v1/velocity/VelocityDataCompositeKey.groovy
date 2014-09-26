package fraud.rest.v1.velocity

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

import javax.persistence.Column
import javax.persistence.EmbeddedId

/**
 * Composite cluster+partition key for velocity_data
 */
@ToString
@EqualsAndHashCode
class VelocityDataCompositeKey implements Serializable {
    @EmbeddedId
    VelocityPartitionKey id;
    @Column(name = "related_metric_type")
    String relatedMetricType;
    @Column(name = "create_date")
    Date createDate;
}
