package spring

import org.springframework.core.io.ClassPathResource
import ru.forxy.common.rest.client.transport.HttpClientSSLKeyStore
import ru.forxy.common.rest.client.transport.HttpClientTransport
import ru.forxy.auth.rest.v1.AuthServiceClient

def String env = System.getenv()['env'] ?: 'dev'

beans {
    def c = new ConfigSlurper(env).parse(new ClassPathResource('spring/auth.client.test.config.groovy').URL)

    sslKeystore(HttpClientSSLKeyStore, new ClassPathResource('cert/oauthTrustStore.jks').inputStream, '5ecret0AUTHPa55word', false)

    httpTransport(HttpClientTransport, ref(sslKeystore))

    authServiceClient(AuthServiceClient,
            c.authServiceClient.endpoint,
            c.authServiceClient.clientId,
            ref(httpTransport))
}
