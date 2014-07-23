package spring

import ru.forxy.common.rest.client.transport.HttpClientTransport
import ru.forxy.user.rest.v1.UserServiceClient

beans {
    httpTransport(HttpClientTransport)

    userServiceClient(UserServiceClient,
            '${ru.forxy.user.UserServiceClient/endpoint}',
            '${ru.forxy.user.UserServiceClient/clientID}',
            ref(httpTransport))
}
