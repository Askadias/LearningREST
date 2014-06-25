package spring

import org.springframework.core.io.ClassPathResource
import ru.forxy.common.rest.client.transport.HttpClientTransport
import ru.forxy.user.rest.v1.UserServiceClient

def String env = System.getenv()['env'] ?: 'dev'

beans {
    def c = new ConfigSlurper(env).parse(new ClassPathResource('spring/user.client.test.config.groovy').URL)

    httpTransport(HttpClientTransport)

    userServiceClient(UserServiceClient,
            c.userServiceClient.endpoint,
            c.userServiceClient.clientId,
            ref(httpTransport))
}
