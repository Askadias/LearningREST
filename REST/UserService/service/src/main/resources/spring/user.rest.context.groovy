package spring

import net.sf.oval.Validator
import net.sf.oval.configuration.xml.XMLConfigurer
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

    jaxrs.server(id: 'userService', address: '/rest/v1') {
        jaxrs.serviceBeans {
            ref(bean: 'userServiceImpl')
            ref(bean: 'systemStatusServiceImpl')
        }
        jaxrs.providers {
            ref(bean: 'jsonValidationProvider')
            ref(bean: 'runtimeExceptionMapper')
        }
    }
}
