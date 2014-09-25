package ru.forxy.auth.rest.v1

import ru.forxy.fraud.rest.v1.velocity.VelocityMetric
import ru.forxy.fraud.rest.v1.velocity.redis.VMetric

interface IVelocityServiceClient {

    List<VelocityMetric> check(final String transactionGUID, final Map<String, String> metrics);
    List<VMetric> rcheck(final String transactionGUID, final Map<String, String> metrics);
}
