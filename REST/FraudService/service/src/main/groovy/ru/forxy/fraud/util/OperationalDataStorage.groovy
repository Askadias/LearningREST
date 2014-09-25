package ru.forxy.fraud.util

import org.springframework.beans.factory.InitializingBean
import ru.forxy.fraud.db.dao.IVelocityConfigDAO
import ru.forxy.fraud.rest.v1.velocity.VelocityConfig

import java.util.concurrent.ConcurrentHashMap

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
