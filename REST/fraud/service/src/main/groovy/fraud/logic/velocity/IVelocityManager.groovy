package fraud.logic.velocity

import org.joda.time.DateTime

/**
 * Black lists manipulation logic API
 */
interface IVelocityManager {

    def cassandraGetMetrics(final Map<String, String[]> velocityRQ, final boolean asyncUpdate)

    def redisGetMetrics(final Map<String, String[]> velocityRQ, final boolean asyncUpdate)

    def cassandraGetHistory(final Map<String, String> filter, final DateTime startDate, final DateTime endDate)

    def redisGetHistory(final Map<String, String> filter, final DateTime startDate, final DateTime endDate)
}
