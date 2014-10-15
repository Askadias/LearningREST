package fraud.logic.velocity
/**
 * Black lists manipulation logic API
 */
interface IVelocityManager {

    def checkCassandraAsync(final Map<String, String[]> velocityRQ)

    def checkRedisAsync(final Map<String, String[]> velocityRQ)
}
