package fraud.rest.v1

import fraud.rest.v1.velocity.Aggregation

interface IFraudServiceClient {

    Map<Map<String, String>, Map<Aggregation, Double>> check(
            final String transactionGUID, final Map<String, String[]> velocityRQ);

    Map<Map<String, String>, Map<Aggregation, Double>> rcheck(
            final String transactionGUID, final Map<String, String[]> velocityRQ);
}
