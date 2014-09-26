package fraud.util

import org.springframework.beans.factory.InitializingBean
import fraud.db.dao.IVelocityConfigDAO
import fraud.rest.v1.velocity.VelocityConfig

/**
 * Shared storage for operational data
 */
class OperationalDataStorage implements InitializingBean {

    IVelocityConfigDAO velocityConfigDAO

    Map<String, VelocityConfig> configsByMetricType

    void invalidate() {
        configsByMetricType = velocityConfigDAO?.findAll()?.groupBy { it.metricType } as Map<String, VelocityConfig>
    }

    @Override
    void afterPropertiesSet() throws Exception {
        invalidate()
    }
}
