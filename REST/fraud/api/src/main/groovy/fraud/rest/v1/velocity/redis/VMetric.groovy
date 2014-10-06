package fraud.rest.v1.velocity.redis

/**
 * Created by Tiger on 24.09.14.
 */
class VMetric {
    String type
    String value
    Map<String, Map<Aggregation, Double>> metrics
}
