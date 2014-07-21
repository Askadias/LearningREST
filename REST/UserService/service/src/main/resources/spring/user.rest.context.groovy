package spring

import net.sf.oval.Validator
import net.sf.oval.configuration.xml.XMLConfigurer
import org.apache.cxf.jaxrs.client.JAXRSClientFactoryBean
import org.apache.cxf.rs.security.oauth2.filters.AccessTokenValidatorClient
import org.apache.cxf.rs.security.oauth2.filters.OAuthRequestFilter
import org.apache.cxf.rs.security.oauth2.provider.OAuthJSONProvider
import org.apache.cxf.rs.security.oauth2.services.AccessTokenService
import org.apache.cxf.rs.security.oauth2.services.AccessTokenValidatorService
import org.apache.cxf.rs.security.oauth2.services.AuthorizationCodeGrantService
import ru.forxy.common.exceptions.support.RuntimeExceptionMapper
import ru.forxy.common.support.Configuration
import ru.forxy.common.web.JSONValidationProvider

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

    tvServiceClientFactory(JAXRSClientFactoryBean) {
        address = '${ru.forxy.user.AccessTokenValidatorClient/endpoint}'
        headers = ['Accept': 'application/xml']
    }

    tokenValidator(AccessTokenValidatorClient) {
        tokenValidatorClient = { bean ->
            bean.factoryBean = 'tvServiceClientFactory'
            bean.factoryMethod = 'createWebClient'
        }
    }

    oauthFiler(OAuthRequestFilter) {
        dataProvider = ref(oauthProvider)
        tokenValidator = ref(tokenValidator)
    }

    accessTokenServiceEndpoint(AccessTokenService) {
        dataProvider = ref(oauthProvider)
    }

    accessTokenValidatorServiceEndpoint(AccessTokenValidatorService) {
        dataProvider = ref(oauthProvider)
    }

    authorizationServiceEndpoint(AuthorizationCodeGrantService) {
        dataProvider = ref(oauthProvider)
    }

    oAuthJSONProvider(OAuthJSONProvider)

    jaxrs.server(id: 'oauthServer', address: '/oauth') {
        jaxrs.serviceBeans {
            ref(bean: 'accessTokenServiceEndpoint')
            ref(bean: 'accessTokenValidatorServiceEndpoint')
            ref(bean: 'authServiceEndpoint')
        }
        jaxrs.providers {
            ref(bean: 'oAuthJSONProvider')
            ref(bean: 'runtimeExceptionMapper')
        }
    }

    jaxrs.server(id: 'loginService', address: '/auth') {
        jaxrs.serviceBeans {
            ref(bean: 'authServiceEndpoint')
        }
        jaxrs.providers {
            ref(bean: 'jsonValidationProvider')
            ref(bean: 'runtimeExceptionMapper')
        }
    }

    jaxrs.server(id: 'userService', address: '/rest/v1') {
        jaxrs.serviceBeans {
            ref(bean: 'userServiceEndpoint')
            ref(bean: 'systemStatusServiceEndpoint')
            ref(bean: 'authorizationServiceEndpoint')
        }
        jaxrs.providers {
            ref(bean: 'jsonValidationProvider')
            ref(bean: 'runtimeExceptionMapper')
            //ref(bean: 'oauthFiler')
        }
    }
}