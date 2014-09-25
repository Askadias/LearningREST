package velocity

import org.springframework.beans.factory.annotation.Autowired

/**
 * Created by Tiger on 25.09.14.
 */
class CacheInvalidateJob {
    @Autowired
    OperationalDataStorage operationalDataStorage

    static triggers = {
        cron name: 'cronTrigger', startDelay: 0, cronExpression: '0 0/1 * * * ?'
    }

    def execute() {
        operationalDataStorage.invalidate()
    }
}