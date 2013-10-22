package ru.forxy.common.exceptions;

import javax.ws.rs.core.Response;

/**
 * Common Validation exception implementation
 */
public class ValidationException extends ServiceException {

    public ValidationException(String message) {
        super(Response.Status.BAD_REQUEST, message);
    }

    public ValidationException(Throwable cause) {
        super(Response.Status.BAD_REQUEST, cause);
    }

    public ValidationException(String message, Throwable cause) {
        super(Response.Status.BAD_REQUEST,  message, cause);
    }
}
