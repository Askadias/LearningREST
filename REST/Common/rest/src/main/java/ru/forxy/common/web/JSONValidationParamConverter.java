package ru.forxy.common.web;

import net.sf.oval.ConstraintViolation;
import net.sf.oval.IValidator;
import net.sf.oval.exception.ValidationFailedException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.ObjectMapper;
import ru.forxy.common.exceptions.ValidationException;
import ru.forxy.common.rest.client.transport.support.ObjectMapperProvider;
import ru.forxy.common.support.Configuration;

import javax.ws.rs.ext.ParamConverter;
import javax.ws.rs.ext.ParamConverterProvider;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Custom JSON provider with validation step
 */
@Provider
public class JSONValidationParamConverter implements
        ParamConverterProvider {

    private IValidator validator;
    private boolean isObjectValidationEnabled = false;
    private ObjectMapper mapper;

    public JSONValidationParamConverter() throws IOException {
        super();
        mapper = ObjectMapperProvider.getDefaultMapper();
    }

    @Override
    public <T> ParamConverter<T> getConverter(final Class<T> rawType,
                                              final Type genericType, final Annotation[] annotations) {

        return new ParamConverter<T>() {
            @Override
            public T fromString(final String value) {
                T object = null;
                try {
                    JsonParser jp = mapper.getJsonFactory().createJsonParser(value);
                    jp.disable(JsonParser.Feature.AUTO_CLOSE_SOURCE);
                    object = mapper.readValue(jp, mapper.constructType(genericType));
                } catch (IOException e) {
                    throw new ValidationException(e);
                }

                // Apply validation
                if (isObjectValidationEnabled) {
                    try {
                        List<ConstraintViolation> violations;
                        violations = validator.validate(object);
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
                return object;
            }

            @Override
            public String toString(final T value) {
                try {
                    return mapper.writer().writeValueAsString(value);
                } catch (Exception e) {
                    throw new ValidationException(e);
                }
            }
        };
    }

    public void setValidator(final IValidator validator) {
        this.validator = validator;
    }

    public void setConfiguration(final Configuration configuration) {
        if (configuration != null) {
            isObjectValidationEnabled =
                    configuration.getBoolean(JSONValidationProvider.Configs.IsObjectValidationEnabled);
        }
    }
}