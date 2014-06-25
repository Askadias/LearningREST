package spring

import org.springframework.aop.framework.ProxyFactoryBean
import ru.forxy.common.rest.SystemStatusServiceImpl
import ru.forxy.user.logic.SystemStatusFacade
import ru.forxy.user.logic.UserServiceFacade
import ru.forxy.user.rest.v1.UserServiceImpl

beans {

    // ================= DAO PROXY ==============================================================================

    userDAOMongoProxy(ProxyFactoryBean) { bean ->
        bean.scope = 'prototype'
        proxyInterfaces = ['ru.forxy.user.db.dao.IUserDAO']
        target = ref(userDAOMongo)
        interceptorNames = ['daoLoggingInterceptor']
    }

    // ================= FACADES ================================================================================

    systemStatusFacade(SystemStatusFacade) {
        userDAO = ref(userDAOMongoProxy)
    }

    userServiceFacade(UserServiceFacade) { bean ->
        bean.autowire = 'byName'
        userDAO = ref(userDAOMongoProxy)
    }

    // ================= SERVICE PROXIES ========================================================================

    systemStatusFacadeProxy(ProxyFactoryBean) { bean ->
        bean.scope = 'prototype'
        proxyInterfaces = ['ru.forxy.common.status.ISystemStatusFacade']
        target = ref(systemStatusFacade)
        interceptorNames = ['serviceLoggingInterceptor']
    }

    userServiceFacadeProxy(ProxyFactoryBean) { bean ->
        bean.scope = 'prototype'
        proxyInterfaces = ['ru.forxy.user.logic.IUserServiceFacade']
        target = ref(userServiceFacade)
        interceptorNames = ['serviceLoggingInterceptor']
    }

    // ================= ENDPOINTS ==============================================================================

    systemStatusServiceImpl(SystemStatusServiceImpl) {
        systemStatusFacade = ref(systemStatusFacadeProxy)
    }

    userServiceImpl(UserServiceImpl) {
        userServiceFacade = ref(userServiceFacadeProxy)
    }
}
