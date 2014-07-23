package spring

import net.sf.oval.Validator
import net.sf.oval.configuration.xml.XMLConfigurer
import org.apache.cxf.jaxrs.client.JAXRSClientFactoryBean
import org.apache.cxf.jaxrs.provider.RequestDispatcherProvider
import org.apache.cxf.rs.security.oauth2.filters.AccessTokenValidatorClient
import org.apache.cxf.rs.security.oauth2.filters.OAuthRequestFilter
import org.apache.cxf.rs.security.oauth2.provider.OAuthJSONProvider
import org.apache.cxf.rs.security.oauth2.services.AccessTokenService
import org.apache.cxf.rs.security.oauth2.services.AccessTokenValidatorService
import org.apache.cxf.rs.security.oauth2.services.AuthorizationCodeGrantService
import ru.forxy.auth.rest.support.SecurityContextFilter
import ru.forxy.common.exceptions.support.RuntimeExceptionMapper
import ru.forxy.common.support.Configuration
import ru.forxy.common.web.JSONValidationProvider

beans {
    xmlns jaxrs: "http://cxf.apache.org/jaxrs"

    def String configDir = System.getProperty('config.dir')

    configuration(Configuration) {
        settings = [(JSONValidationProvider.Configs.IsObjectValidationEnabled): 'true']
    }

    xmlConfigurer(XMLConfigurer, new File("$configDir/AuthService/appconfig/base/ru.forxy.auth.validation.xml"))

    validator(Validator, ref(xmlConfigurer))

    jsonValidationProvider(JSONValidationProvider) {
        configuration = ref(configuration)
        validator = ref(validator)
    }

    runtimeExceptionMapper(RuntimeExceptionMapper)

    tvServiceClientFactory(JAXRSClientFactoryBean) {
        address = '${ru.forxy.auth.AccessTokenValidatorClient/endpoint}'
        headers = ['Accept': 'application/xml']
    }

    tokenValidator(AccessTokenValidatorClient) {
        tokenValidatorClient = { bean ->
            bean.factoryBean = 'tvServiceClientFactory'
            bean.factoryMethod = 'createWebClient'
        }
    }

    authorizationServiceEndpoint(AuthorizationCodeGrantService) {
        dataProvider = ref(oauthProvider)
    }

    oAuthJSONProvider(OAuthJSONProvider)

    // ------ Token grant service endpoint configuration -------------------------------

    oauthFiler(OAuthRequestFilter) {
        dataProvider = ref(oauthProvider)
        //tokenValidator = ref(tokenValidator)
    }

    accessTokenServiceEndpoint(AccessTokenService) {
        dataProvider = ref(oauthProvider)
    }

    accessTokenValidatorServiceEndpoint(AccessTokenValidatorService) {
        dataProvider = ref(oauthProvider)
    }

    // ------ Authentication and Authorization endpoints configuration -----------------

    securityContextFilter(SecurityContextFilter) {
        userServiceClient = ref(userServiceClient)
    }
    socialViews(RequestDispatcherProvider) {
        classResources = ['org.apache.cxf.rs.security.oauth2.common.OAuthAuthorizationData': '/app/authorize']
        beanNames = ['org.apache.cxf.rs.security.oauth2.common.OAuthAuthorizationData': 'client']
        logRedirects = true
    }

    // ------ Clients management endpoint configuration --------------------------------


    jaxrs.server(id: 'oauthServer', address: '/oauth') {
        jaxrs.serviceBeans {
            ref(bean: 'accessTokenServiceEndpoint')
            ref(bean: 'accessTokenValidatorServiceEndpoint')
            ref(bean: 'authorizationServiceEndpoint')
        }
        jaxrs.providers {
            ref(bean: 'oAuthJSONProvider')
            ref(bean: 'socialViews')
            ref(bean: 'securityContextFilter')
        }
    }

    jaxrs.server(id: 'authService', address: '/rest/v1') {
        jaxrs.serviceBeans {
            ref(bean: 'tokenGrantServiceEndpoint')
            ref(bean: 'clientServiceEndpoint')
            ref(bean: 'systemStatusServiceEndpoint')
        }
        jaxrs.providers {
            ref(bean: 'jsonValidationProvider')
            ref(bean: 'runtimeExceptionMapper')
            ref(bean: 'oauthFiler')
        }
    }
}