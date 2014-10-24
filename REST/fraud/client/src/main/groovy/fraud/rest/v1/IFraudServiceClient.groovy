package fraud.rest.v1

import fraud.rest.v1.velocity.Velocity

interface IFraudServiceClient {

    List<Velocity> check(final String transactionGUID, final Map<String, String[]> velocityRQ);

    List<Velocity> rcheck(final String transactionGUID, final Map<String, String[]> velocityRQ);
}
