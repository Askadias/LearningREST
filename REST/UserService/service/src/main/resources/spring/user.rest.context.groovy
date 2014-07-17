package spring

import net.sf.oval.Validator
import net.sf.oval.configuration.xml.XMLConfigurer
import org.apache.cxf.rs.security.oauth2.filters.OAuthRequestFilter
import org.apache.cxf.rs.security.oauth2.provider.OAuthJSONProvider
import org.apache.cxf.rs.security.oauth2.services.AccessTokenService
import org.apache.cxf.rs.security.oauth2.services.AccessTokenValidatorService
import org.apache.cxf.rs.security.oauth2.services.AuthorizationCodeGrantService
import ru.forxy.common.exceptions.support.RuntimeExceptionMapper
import ru.forxy.common.support.Configuration
import ru.forxy.common.web.JSONValidationProvider
import ru.forxy.user.rest.v1.OAuthManager

beans {
    xmlns jaxrs: "http://cxf.apache.org/jaxrs"

    def String configDir = System.getProperty('config.dir')

    configuration(Configuration) {
        settings = [(JSONValidationProvider.Configs.IsObjectValidationEnabled): 'true']
    }

    xmlConfigurer(XMLConfigurer, new File("$configDir/UserService/appconfig/base/ru.forxy.user.validation.xml"))

    validator(Validator, ref(xmlConfigurer))

    jsonValidationProvider(JSONValidationProvider) {
        configuration = ref(configuration)
        validator = ref(validator)
    }

    runtimeExceptionMapper(RuntimeExceptionMapper)

    oauthProvider(OAuthManager) {
        userServiceFacade = ref(userServiceFacadeProxy)
    }

    oauthFiler(OAuthRequestFilter) {
        dataProvider = ref(oauthProvider)
    }

    accessTokenService(AccessTokenService) {
        dataProvider = ref(oauthProvider)
    }

    accessTokenValidatorService(AccessTokenValidatorService) {
        dataProvider = ref(oauthProvider)
    }

    authorizationService(AuthorizationCodeGrantService) {
        dataProvider = ref(oauthProvider)
    }

    oAuthJSONProvider(OAuthJSONProvider)

    jaxrs.server(id: 'oauthServer', address: '/oauth') {
        jaxrs.serviceBeans {
            ref(bean: 'accessTokenService')
            ref(bean: 'accessTokenValidatorService')
        }
        jaxrs.providers {
            ref(bean: 'oAuthJSONProvider')
        }
    }

    jaxrs.server(id: 'userService', address: '/rest/v1') {
        jaxrs.serviceBeans {
            ref(bean: 'userServiceImpl')
            //ref(bean: 'authServiceImpl')
            ref(bean: 'clientServiceImpl')
            ref(bean: 'systemStatusServiceImpl')
            ref(bean: 'authorizationService')
        }
        jaxrs.providers {
            ref(bean: 'jsonValidationProvider')
            ref(bean: 'runtimeExceptionMapper')
            //ref(bean: 'oauthFiler')
        }
    }
}