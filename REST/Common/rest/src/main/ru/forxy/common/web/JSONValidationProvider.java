package ru.forxy.common.web;

import net.sf.oval.ConstraintViolation;
import net.sf.oval.IValidator;
import net.sf.oval.exception.ValidationFailedException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.jaxrs.JacksonJsonProvider;
import org.codehaus.jackson.map.JsonMappingException;
import ru.forxy.common.exceptions.ValidationException;
import ru.forxy.common.support.Configuration;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Custom JSON provider with validation step
 */
public class JSONValidationProvider extends JacksonJsonProvider {

    public static enum Configs {
        IsObjectValidationEnabled
    }

    private final static String PROFILE_REGISTER = "create";
    private final static String PROFILE_DEFAULT = "default";
    private final static String PROFILE_UPDATE = "update";

    private IValidator validator;
    private boolean isObjectValidationEnabled = false;

    @Context
    private HttpServletRequest servletRequest;

    public JSONValidationProvider() throws IOException {
        super();
    }

    @Override
    public Object readFrom(final Class<Object> type, final Type genericType, final Annotation[] annotations,
                           final MediaType mediaType, final MultivaluedMap<String, String> headers,
                           final InputStream is) throws IOException {
        final Object value;
        try {
            value = super.readFrom(type, genericType, annotations, mediaType, headers, is);
        } catch (JsonParseException e) {
            throw new ValidationException(e);
        } catch (JsonMappingException e) {
            throw new ValidationException(e);
        }

        // Apply validation
        if (isObjectValidationEnabled) {
            try {
                List<ConstraintViolation> violations = null;

                if (servletRequest.getMethod().equals(HttpMethod.POST)) { // create

                    violations = validator.validate(value, PROFILE_REGISTER, PROFILE_DEFAULT);

                } else if (servletRequest.getMethod().equals(HttpMethod.PUT)) { // update

                    violations = validator.validate(value, PROFILE_UPDATE, PROFILE_DEFAULT);

                }
                if (violations != null && violations.size() > 0) {

                    final List<String> messages = new ArrayList<String>(violations.size());

                    for (ConstraintViolation violation : violations) {
                        messages.add(violation.getMessage());
                    }

                    throw new ValidationException(messages);
                }
            } catch (ValidationFailedException e) {
                throw ValidationException.build(e);
            }
        }
        return value;
    }

    public void setValidator(final IValidator validator) {
        this.validator = validator;
    }

    public void setConfiguration(final Configuration configuration) {
        if (configuration != null) {
            isObjectValidationEnabled = configuration.getBoolean(Configs.IsObjectValidationEnabled);
        }
    }
}