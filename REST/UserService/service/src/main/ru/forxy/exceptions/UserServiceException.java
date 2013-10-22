package ru.forxy.exceptions;

import ru.forxy.common.exceptions.ServiceException;

import javax.ws.rs.core.Response;

/**
 * Common UserService exception
 */
public class UserServiceException extends ServiceException {

    public UserServiceException(String message) {
        super(message);
    }

    public UserServiceException(Throwable cause) {
        super(cause);
    }

    public UserServiceException(Response.Status status, String message) {
        super(status, message);
    }

    public UserServiceException(Response.Status status, Throwable cause) {
        super(status, cause);
    }

    public UserServiceException(Response.Status status, String message, Throwable cause) {
        super(status, message, cause);
    }
}
