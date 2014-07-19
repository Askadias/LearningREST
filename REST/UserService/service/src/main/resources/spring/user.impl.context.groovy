package spring

import org.springframework.aop.framework.ProxyFactoryBean
import org.springframework.security.crypto.password.StandardPasswordEncoder
import ru.forxy.common.rest.SystemStatusServiceEndpoint
import ru.forxy.user.logic.OAuthManager
import ru.forxy.user.logic.SystemStatusFacade
import ru.forxy.user.logic.UserServiceFacade
import ru.forxy.user.rest.v1.AuthServiceEndpoint
import ru.forxy.user.rest.v1.UserServiceEndpoint

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

    passwordEncoder(StandardPasswordEncoder)

    userServiceFacade(UserServiceFacade) { bean ->
        bean.autowire = 'byName'
        userDAO = ref(userDAOMongoProxy)
        passwordEncoder = ref(passwordEncoder)
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

    // ================= OAUTH ==================================================================================

    oauthProvider(OAuthManager) {
        userServiceFacade = ref(userServiceFacadeProxy)
    }

    // ================= ENDPOINTS ==============================================================================

    systemStatusServiceEndpoint(SystemStatusServiceEndpoint) {
        systemStatusFacade = ref(systemStatusFacadeProxy)
    }

    userServiceEndpoint(UserServiceEndpoint) {
        userServiceFacade = ref(userServiceFacadeProxy)
    }

    authServiceEndpoint(AuthServiceEndpoint) {
        userServiceFacade = ref(userServiceFacadeProxy)
    }
}
