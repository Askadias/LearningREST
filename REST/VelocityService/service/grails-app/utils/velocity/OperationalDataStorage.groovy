package velocity

/**
 * Created by Tiger on 25.09.14.
 */
class OperationalDataStorage {
    Map<String, VelocityConfig> configs;

    void invalidate() {
        configs = new HashMap<>()
        VelocityConfig.all.each { configs << [(it.metricType): it]}
    }

    Map<String, VelocityConfig> getConfigs() {
        if (!configs) invalidate()
        return configs
    }
}
