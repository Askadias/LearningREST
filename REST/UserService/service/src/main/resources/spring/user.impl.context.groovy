package spring

import ru.forxy.common.rest.SystemStatusServiceImpl
import ru.forxy.user.logic.SystemStatusFacade
import ru.forxy.user.logic.UserServiceFacade
import ru.forxy.user.rest.v1.UserServiceImpl

beans {

    systemStatusFacade(SystemStatusFacade) {
        userDAO = ref(userDAOImplMongo)
    }

    systemStatusServiceImpl(SystemStatusServiceImpl) {
        systemStatusFacade = ref(systemStatusFacade)
    }

    userServiceFacade(UserServiceFacade) { bean ->
        bean.autowire = 'byName'
        userDAO = ref(userDAOImplMongo)
    }

    userServiceImpl(UserServiceImpl) {
        userServiceFacade = ref(userServiceFacade)
    }
}
