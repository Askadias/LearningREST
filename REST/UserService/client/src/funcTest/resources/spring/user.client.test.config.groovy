package spring

userServiceClient {
    endpoint = 'MOST_OVERRIDE'
    clientId = 'test'
}

environments {
    dev {
        userServiceClient.endpoint = 'https://localhost:10090/UserService/service/rest/v1/'
    }
    integration {
        userServiceClient.endpoint = 'https://localhost:10090/UserService/service/rest/v1/'
    }
    ppe {
        userServiceClient.endpoint = 'https://localhost:10090/UserService/service/rest/v1/'
    }
    prod {
        userServiceClient.endpoint = 'https://localhost:10090/UserService/service/rest/v1/'
    }
}