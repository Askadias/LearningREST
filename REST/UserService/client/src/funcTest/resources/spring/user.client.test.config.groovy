package spring

userServiceClient {
    endpoint = 'MOST_OVERRIDE'
    clientId = 'test'
}

environments {
    dev {
        userServiceClient.endpoint = 'http://localhost:10080/UserService/service/rest/v1/'
    }
    integration {
        userServiceClient.endpoint = 'http://localhost:10080/UserService/service/rest/v1/'
    }
    ppe {
        userServiceClient.endpoint = 'http://localhost:10080/UserService/service/rest/v1/'
    }
    prod {
        userServiceClient.endpoint = 'http://localhost:10080/UserService/service/rest/v1/'
    }
}