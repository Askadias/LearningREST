package spring

mongo {
    host = 'MOST_OVERRIDE'
    port = 27017
    connectionsPerHost = 5
    connectTimeout = 30000
    maxWaitTime = 10000
    writeNumber = 1
    writeTimeout = 0
    writeFsync = true
}

environments {
    dev {
        mongo.host = 'localhost'
    }
    integration {
        mongo.host = 'localhost'
    }
    ppe {
        mongo.host = 'localhost'
    }
    prod {
        mongo.host = 'localhost'
    }
}