package ru.forxy.auth.rest.support;

import com.nimbusds.jose.JOSEException;
import org.codehaus.jackson.jaxrs.JacksonJsonProvider;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.InitializingBean;
import ru.forxy.auth.exceptions.AuthServiceEventLogId;
import ru.forxy.auth.rest.v1.pojo.User;
import ru.forxy.auth.security.JWTManager;
import ru.forxy.common.exceptions.ServiceException;
import ru.forxy.common.logging.exceptions.LoggingCommonEventLogId;
import ru.forxy.common.rest.client.transport.support.ObjectMapperProvider;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

/**
 * Custom JSON provider with validation step
 */
public class JWTProvider extends JacksonJsonProvider {

    private ObjectMapper mapper = ObjectMapperProvider.getDefaultMapper();

    private JWTManager jwtManager;

    public JWTProvider() throws IOException {
        super();
        setMapper(mapper);
    }

    @Override
    public void writeTo(Object value, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType,
                        MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream) throws IOException {
        if (value instanceof User) {
            try {
                value = jwtManager.toJWT(mapper.writeValueAsString(value));
                type = String.class;
            } catch (JOSEException e) {
                throw new ServiceException(LoggingCommonEventLogId.UnknownServiceException, e);
            }
        }
        super.writeTo(value, type, genericType, annotations, mediaType, httpHeaders, entityStream);
    }

    public void setJwtManager(JWTManager jwtManager) {
        this.jwtManager = jwtManager;
    }
}
