package spring

import net.sf.oval.Validator
import net.sf.oval.configuration.xml.XMLConfigurer
import ru.forxy.common.exceptions.support.RuntimeExceptionMapper
import ru.forxy.common.support.Configuration
import ru.forxy.common.web.JSONValidationProvider

beans {
    //def String catalinabase = System.getenv()['TOMCAT_HOME']
    def String catalinabase = '/usr/local/tomcat/forxy'
    xmlns jaxrs: "http://cxf.apache.org/jaxrs"

    def Map<Object, String> confProps = new HashMap<Object, String>();
    confProps.put(JSONValidationProvider.Configs.IsObjectValidationEnabled, "true");

    configuration(Configuration, confProps)

    xmlConfigurer(XMLConfigurer, new File("${catalinabase}/conf/UserService/appconfig/base/ru.forxy.user.validation.xml"))

    validator(Validator, ref(xmlConfigurer))

    jsonValidationProvider(JSONValidationProvider) {
        configuration = ref(configuration)
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
