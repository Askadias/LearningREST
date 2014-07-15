package spring

import org.springframework.core.io.ClassPathResource
import ru.forxy.common.rest.client.transport.HttpClientSSLKeyStore
import ru.forxy.common.rest.client.transport.HttpClientTransport
import ru.forxy.user.rest.v1.UserServiceClient

def String env = System.getenv()['env'] ?: 'dev'

beans {
    def c = new ConfigSlurper(env).parse(new ClassPathResource('spring/user.client.test.config.groovy').URL)

    sslKeystore(HttpClientSSLKeyStore, new ClassPathResource('cert/oauthTrustStore.jks').inputStream, '5ecret0AUTHPa55word', false)

    httpTransport(HttpClientTransport, ref(sslKeystore))

    userServiceClient(UserServiceClient,
            c.userServiceClient.endpoint,
            c.userServiceClient.clientId,
            ref(httpTransport))
}
