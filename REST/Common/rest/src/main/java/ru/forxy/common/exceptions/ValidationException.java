package ru.forxy.common.exceptions;

import net.sf.oval.exception.ValidationFailedException;
import org.apache.commons.lang.ArrayUtils;
import ru.forxy.common.exceptions.support.ResponseBuilder;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

/**
 * Generic validation exception
 */
public class ValidationException extends ServiceException {

    private static final long serialVersionUID = 2500370557195275595L;

    private final List<String> messages;

    public ValidationException(List<String> messages) {
        super(RESTCommonEventLogId.ValidationException);
        this.messages = messages;
    }

    public ValidationException(String message) {
        super(message, RESTCommonEventLogId.ValidationException);
        messages = new ArrayList<>(1);
        messages.add(message);
    }

    public ValidationException(Throwable cause, String message) {
        super(cause, message, RESTCommonEventLogId.ValidationException);
        messages = new ArrayList<>(1);
        messages.add(super.getMessage());
    }

    public ValidationException(Throwable cause, Object... args) {
        super(cause, RESTCommonEventLogId.ValidationException, args);
        messages = new ArrayList<>(1);
        messages.add(super.getMessage());
    }

    public ValidationException(Object... args) {
        super(RESTCommonEventLogId.ValidationException, args);
        messages = new ArrayList<>(1);
        messages.add(super.getMessage());
    }

    public ValidationException(Throwable cause) {
        super(cause, RESTCommonEventLogId.ValidationException);
        messages = new ArrayList<>(1);
        messages.add(super.getMessage());
    }

    public List<String> getMessages() {
        return messages;
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
