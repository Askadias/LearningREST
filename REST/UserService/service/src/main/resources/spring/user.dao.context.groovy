package spring

import org.springframework.core.io.ClassPathResource
import org.springframework.data.mongodb.core.MongoTemplate
import ru.forxy.user.db.dao.mongo.UserDAO

beans {
    def String env = System.getenv()['env'] ?: 'dev'
    def ConfigObject c = new ConfigSlurper(env).parse(new ClassPathResource('spring/user.config.groovy').URL)
    xmlns context:'http://www.springframework.org/schema/context'
    xmlns mongo:'http://www.springframework.org/schema/data/mongo'

    context.'component-scan'('base-package': 'ru.forxy.user.rest.pojo')

    mongo.mongo(id : 'mongo', host = c.mongo.host, port : c.mongo.port) {
        mongo.options(
                'connections-per-host' : c.mongo.connectionsPerHost,
                'connect-timeout' : c.mongo.connectTimeout,
                'max-wait-time' : c.mongo.maxWaitTime,
                'write-number' : c.mongo.writeNumber,
                'write-timeout' : c.mongo.writeTimeout,
                'write-fsync' : c.mongo.writeFsync
        )
    }

    def mongoBean = ref('mongo')
    def String mongoRefName = 'mongo'

    mongo.'db-factory'( id: 'db-factory', dbname : 'user', 'mongo-ref' : mongoRefName)

    mongo.repositories( 'base-package' : 'ru.forxy.db.dao.mongo')

    mongoTemplate(MongoTemplate, ref('db-factory'))

    userDAOImplMongo(UserDAO) { bean ->
        bean.autowire = 'byName'
        mongoTemplate = ref(mongoTemplate)
    }
}
