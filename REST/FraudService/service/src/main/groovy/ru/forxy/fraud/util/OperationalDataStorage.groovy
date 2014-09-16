package ru.forxy.fraud.util

import org.springframework.beans.factory.InitializingBean
import ru.forxy.fraud.db.dao.IVelocityConfigDAO
import ru.forxy.fraud.rest.v1.velocity.VelocityConfig

/**
 * Shared storage for operational data
 */
class OperationalDataStorage implements InitializingBean {

    IVelocityConfigDAO velocityConfigDAO

    Map<String, VelocityConfig> configsByMetricType

    void invalidate() {
        configsByMetricType = new HashMap<>()
        if (velocityConfigDAO != null) {
            Iterable<VelocityConfig> configs = velocityConfigDAO.findAll()
            if (configs != null) {
                configs.each { configsByMetricType.put(it.getMetricType(), it) }
            }
        }
    }

    @Override
    void afterPropertiesSet() throws Exception {
        invalidate()
    }
}
