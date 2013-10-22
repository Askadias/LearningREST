package ru.forxy.common.exceptions;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

/**
 * Base class for WebService exceptions that keeps basic information to put to the service response
 */
public class ServiceException extends WebApplicationException {

    private Response.Status status;

    public ServiceException(String message) {
        this(Response.Status.INTERNAL_SERVER_ERROR, message, null);
    }

    public ServiceException(Throwable cause) {
        this(Response.Status.INTERNAL_SERVER_ERROR, null, cause);
    }

    public ServiceException(Response.Status status, String message) {
        this(status, message, null);
    }

    public ServiceException(Response.Status status, Throwable cause) {
        this(status, null, cause);
    }

    public ServiceException(Response.Status status, String message, Throwable cause) {
        super(cause, status);
        this.status = status;
    }

    public Response.Status getStatus() {
        return status;
    }
}
