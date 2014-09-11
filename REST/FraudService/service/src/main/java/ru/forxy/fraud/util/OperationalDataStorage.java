package ru.forxy.fraud.util;

import org.springframework.beans.factory.InitializingBean;
import ru.forxy.fraud.db.dao.IVelocityConfigDAO;
import ru.forxy.fraud.rest.v1.velocity.VelocityConfig;

import java.util.HashMap;
import java.util.Map;

/**
 * Shared storage for operational data
 */
public class OperationalDataStorage implements InitializingBean {

    private IVelocityConfigDAO velocityConfigDAO;

    private Map<String, VelocityConfig> configsByMetricType;

    public void invalidate() {
        configsByMetricType = new HashMap<>();
        if (velocityConfigDAO != null) {
            Iterable<VelocityConfig> configs = velocityConfigDAO.findAll();
            if (configs != null) {
                for (VelocityConfig config : configs) {
                    configsByMetricType.put(config.getMetricType(), config);
                }
            }
        }
    }

    public Map<String, VelocityConfig> getConfigsByMetricType() {
        return configsByMetricType;
    }

    public void setVelocityConfigDAO(IVelocityConfigDAO velocityConfigDAO) {
        this.velocityConfigDAO = velocityConfigDAO;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        invalidate();
    }
}
