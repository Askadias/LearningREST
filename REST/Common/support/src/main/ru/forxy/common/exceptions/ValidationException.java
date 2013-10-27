package ru.forxy.common.exceptions;

import net.sf.oval.exception.ValidationFailedException;
import ru.forxy.common.exceptions.utils.ResponseBuilder;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

/**
 * Generic validation exception
 */
public class ValidationException extends BadRequestException {

    private static final long serialVersionUID = 5239154110932452859L;

    public ValidationException(final List<String> messages) {
        super(ResponseBuilder.build(Response.Status.BAD_REQUEST, messages));
    }

    public ValidationException(final String message) {
        super(ResponseBuilder.build(Response.Status.BAD_REQUEST, message));
    }

    public ValidationException(final Throwable cause) {
        super(ResponseBuilder.build(Response.Status.BAD_REQUEST, cause));
    }

    public static ValidationException build(final ValidationFailedException e) {
        final List<String> messages = new ArrayList<String>();
        Throwable cause = e;
        while (cause != null) {
            messages.add(cause.getMessage());
            cause = cause.getCause();
        }
        return new ValidationException(messages);
    }
}
