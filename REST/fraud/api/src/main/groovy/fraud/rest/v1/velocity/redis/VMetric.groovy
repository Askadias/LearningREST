package fraud.rest.v1.velocity.redis

/**
 * Created by Tiger on 24.09.14.
 */
class VMetric implements Serializable {
    String metricType
    String metricValue
    Map<String, Map<Aggregation, Double>> aggregations
}
