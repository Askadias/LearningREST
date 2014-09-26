package fraud.rest.v1

import fraud.rest.v1.velocity.VelocityMetric
import fraud.rest.v1.velocity.redis.VMetric

interface IFraudServiceClient {

    List<VelocityMetric> check(final String transactionGUID, final Map<String, String> metrics);
    List<VMetric> rcheck(final String transactionGUID, final Map<String, String> metrics);
}
