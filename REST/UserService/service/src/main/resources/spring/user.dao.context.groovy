package spring

import org.springframework.data.mongodb.core.MongoTemplate
import ru.forxy.user.db.dao.mongo.UserDAO

beans {
    xmlns context: 'http://www.springframework.org/schema/context'
    xmlns mongo: 'http://www.springframework.org/schema/data/mongo'

    context.'component-scan'('base-package': 'ru.forxy.user.rest.pojo')
    context.'annotation-config'()

    mongo.mongo(id: 'mongo', host = '${ru.forxy.user.MongoTemplate/host}',
            port: '${ru.forxy.user.MongoTemplate/port}') {
        mongo.options(
                'connections-per-host': '${ru.forxy.user.MongoTemplate/connections}',
                'connect-timeout': '${ru.forxy.user.MongoTemplate/timeout}',
                'max-wait-time': '${ru.forxy.user.MongoTemplate/maxWait}',
                'write-number': '${ru.forxy.user.MongoTemplate/writeNumber}',
                'write-timeout': '${ru.forxy.user.MongoTemplate/writeTimeout}',
                'write-fsync': '${ru.forxy.user.MongoTemplate/writeFSync}'
        )
    }

    mongo.'db-factory'(id: 'db-factory', dbname: 'user', 'mongo-ref': 'mongo')

    mongo.repositories('base-package': 'ru.forxy.db.dao.mongo')

    mongoTemplate(MongoTemplate, ref('db-factory'))

    userDAOMongo(UserDAO) { bean ->
        bean.autowire = 'byName'
        mongoTemplate = ref(mongoTemplate)
    }
}
