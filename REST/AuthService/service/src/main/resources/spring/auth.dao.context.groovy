package spring

import org.springframework.data.mongodb.core.MongoTemplate
import ru.forxy.auth.db.dao.mongo.ClientDAO
import ru.forxy.auth.db.dao.mongo.TokenDAO

beans {
    xmlns context: 'http://www.springframework.org/schema/context'
    xmlns mongo: 'http://www.springframework.org/schema/data/mongo'

    context.'component-scan'('base-package': 'ru.forxy.auth.rest.pojo')
    context.'annotation-config'()

    mongo.mongo(id: 'mongo', host = '${ru.forxy.auth.MongoTemplate/host}',
            port: '${ru.forxy.auth.MongoTemplate/port}') {
        mongo.options(
                'connections-per-host': '${ru.forxy.auth.MongoTemplate/connections}',
                'connect-timeout': '${ru.forxy.auth.MongoTemplate/timeout}',
                'max-wait-time': '${ru.forxy.auth.MongoTemplate/maxWait}',
                'write-number': '${ru.forxy.auth.MongoTemplate/writeNumber}',
                'write-timeout': '${ru.forxy.auth.MongoTemplate/writeTimeout}',
                'write-fsync': '${ru.forxy.auth.MongoTemplate/writeFSync}'
        )
    }

    mongo.'db-factory'(id: 'db-factory', dbname: 'auth', 'mongo-ref': 'mongo')

    mongo.repositories('base-package': 'ru.forxy.db.dao.mongo')

    mongoTemplate(MongoTemplate, ref('db-factory'))

    authDAOMongo(TokenDAO) { bean ->
        bean.autowire = 'byName'
        mongoTemplate = ref(mongoTemplate)
    }

    clientDAOMongo(ClientDAO) { bean ->
        bean.autowire = 'byName'
        mongoTemplate = ref(mongoTemplate)
    }
}
