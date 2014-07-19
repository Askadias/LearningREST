package spring

authServiceClient {
    endpoint = 'MOST_OVERRIDE'
    clientId = 'test'
}

environments {
    dev {
        authServiceClient.endpoint = 'http://localhost:11080/AuthService/service/rest/v1/'
    }
    integration {
        authServiceClient.endpoint = 'http://localhost:11080/AuthService/service/rest/v1/'
    }
    ppe {
        authServiceClient.endpoint = 'http://localhost:11080/AuthService/service/rest/v1/'
    }
    prod {
        authServiceClient.endpoint = 'http://localhost:11080/AuthService/service/rest/v1/'
    }
}