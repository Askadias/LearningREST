package fraud.controller.v1

import fraud.controller.v1.velocity.Velocity

interface IFraudServiceClient {

    List<Velocity> check(final String transactionGUID, final Map<String, String[]> velocityRQ);

    List<Velocity> rcheck(final String transactionGUID, final Map<String, String[]> velocityRQ);
}
