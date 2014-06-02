package ru.forxy.common.exceptions.support;

import ru.forxy.common.pojo.ErrorEntity;

import javax.ws.rs.core.Response;
import java.util.List;

import static javax.ws.rs.core.Response.Status;

/**
 * Builder for error messages in the jax-rs response.
 */
public final class ResponseBuilder {

    private ResponseBuilder() {
    }

    public static Response build(final Status status, final String message) {
        final ErrorEntity errorEntity = new ErrorEntity(String.valueOf(status.getStatusCode()), message);
        return Response.status(status).entity(errorEntity).build();
    }

    public static Response build(final Status status, final List<String> messages) {
        final ErrorEntity errorEntity = new ErrorEntity(String.valueOf(status.getStatusCode()), messages);
        return Response.status(status).entity(errorEntity).build();
    }

    public static Response build(final Status status, final Throwable cause) {
        final ErrorEntity errorEntity = new ErrorEntity(String.valueOf(status.getStatusCode()), cause);
        return Response.status(status).entity(errorEntity).build();
    }

    public static Response build(final Status status, final String code, final String message) {
        return Response.status(status).entity(new ErrorEntity(code, message)).build();
    }
}
