package spring

import org.springframework.aop.framework.ProxyFactoryBean
import org.springframework.security.crypto.password.StandardPasswordEncoder
import ru.forxy.common.rest.SystemStatusServiceEndpoint
import ru.forxy.auth.logic.ClientServiceFacade
import ru.forxy.auth.logic.OAuthManager
import ru.forxy.auth.logic.SystemStatusFacade
import ru.forxy.auth.logic.TokenGrantServiceFacade
import ru.forxy.auth.rest.v1.ClientServiceEndpoint
import ru.forxy.auth.rest.v1.TokenGrantServiceEndpoint

beans {

    // ================= DAO PROXY ==============================================================================

    tokenDAOMongoProxy(ProxyFactoryBean) { bean ->
        bean.scope = 'prototype'
        proxyInterfaces = ['ru.forxy.auth.db.dao.ITokenDAO']
        target = ref(authDAOMongo)
        interceptorNames = ['daoLoggingInterceptor']
    }

    clientDAOMongoProxy(ProxyFactoryBean) { bean ->
        bean.scope = 'prototype'
        proxyInterfaces = ['ru.forxy.auth.db.dao.IClientDAO']
        target = ref(clientDAOMongo)
        interceptorNames = ['daoLoggingInterceptor']
    }

    // ================= FACADES ================================================================================

    systemStatusFacade(SystemStatusFacade) {
        tokenDAO = ref(tokenDAOMongoProxy)
    }

    passwordEncoder(StandardPasswordEncoder)

    tokenGrantServiceFacade(TokenGrantServiceFacade) { bean ->
        bean.autowire = 'byName'
        tokenDAO = ref(tokenDAOMongoProxy)
        passwordEncoder = ref(passwordEncoder)
    }

    clientServiceFacade(ClientServiceFacade) { bean ->
        bean.autowire = 'byName'
        clientDAO = ref(clientDAOMongoProxy)
    }

    // ================= SERVICE PROXIES ========================================================================

    systemStatusFacadeProxy(ProxyFactoryBean) { bean ->
        bean.scope = 'prototype'
        proxyInterfaces = ['ru.forxy.common.status.ISystemStatusFacade']
        target = ref(systemStatusFacade)
        interceptorNames = ['serviceLoggingInterceptor']
    }

    tokenGrantServiceFacadeProxy(ProxyFactoryBean) { bean ->
        bean.scope = 'prototype'
        proxyInterfaces = ['ru.forxy.auth.logic.ITokenGrantServiceFacade']
        target = ref(tokenGrantServiceFacade)
        interceptorNames = ['serviceLoggingInterceptor']
    }

    clientServiceFacadeProxy(ProxyFactoryBean) { bean ->
        bean.scope = 'prototype'
        proxyInterfaces = ['ru.forxy.auth.logic.IClientServiceFacade']
        target = ref(clientServiceFacade)
        interceptorNames = ['serviceLoggingInterceptor']
    }

    // ================= OAUTH ==================================================================================

    oauthProvider(OAuthManager) {
        tokenGrantServiceFacade = ref(tokenGrantServiceFacadeProxy)
        clientServiceFacade = ref(clientServiceFacadeProxy)
    }

    // ================= ENDPOINTS ==============================================================================

    systemStatusServiceEndpoint(SystemStatusServiceEndpoint) {
        systemStatusFacade = ref(systemStatusFacadeProxy)
    }

    tokenGrantServiceEndpoint(TokenGrantServiceEndpoint) {
        tokenGrantServiceFacade = ref(tokenGrantServiceFacadeProxy)
    }

    clientServiceEndpoint(ClientServiceEndpoint) {
        clientServiceFacade = ref(clientServiceFacadeProxy)
    }
}
