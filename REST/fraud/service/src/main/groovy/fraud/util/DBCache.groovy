package fraud.util

import org.springframework.beans.factory.InitializingBean
import fraud.db.dao.IVelocityConfigDAO
import fraud.rest.v1.velocity.VelocityConfig

/**
 * Shared storage for operational data
 */
class DBCache implements InitializingBean {

    IVelocityConfigDAO velocityConfigDAO

    Map<String, VelocityConfig> velocityConfigs

    void invalidate() {
        velocityConfigs = new HashMap<String, VelocityConfig>()
        velocityConfigDAO?.findAll()?.each { this.@velocityConfigs << [(it.metricType): it] }
    }

    @Override
    void afterPropertiesSet() throws Exception {
        invalidate()
    }
}
