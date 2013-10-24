package ru.forxy.common.service;

import ru.forxy.common.pojo.ErrorMessage;

import javax.ws.rs.core.Response;
import java.util.List;

import static  javax.ws.rs.core.Response.Status;

/**
 * Builder for error messages in the jax-rs response.
 */
public final class ErrorResponseBuilder {

    private ErrorResponseBuilder()
    {
    }

    public static Response build(final Status status, final String message)
    {
        final ErrorMessage error = new ErrorMessage(String.valueOf(status.getStatusCode()), message);
        return Response.status(status).entity(error).build();
    }

    public static Response build(final Status status, final List<String> messages)
    {
        final ErrorMessage error = new ErrorMessage(String.valueOf(status.getStatusCode()), messages);
        return Response.status(status).entity(error).build();
    }

    public static Response build(final Status status, final Throwable cause)
    {
        final ErrorMessage error = new ErrorMessage(String.valueOf(status.getStatusCode()), cause);
        return Response.status(status).entity(error).build();
    }

    public static Response build(final Status status, final String code, final String message)
    {
        return Response.status(status).entity(new ErrorMessage(code, message)).build();
    }
}
