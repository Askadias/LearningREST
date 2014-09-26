package fraud.rest.v1.velocity

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

/**
 * Related metrics aggregation configuration
 */
@ToString
@EqualsAndHashCode
class AggregationConfig implements Serializable {
    AggregationType type;
    Long period;
}
