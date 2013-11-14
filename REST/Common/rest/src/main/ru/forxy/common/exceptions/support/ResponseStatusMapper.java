package ru.forxy.common.exceptions.support;

import ru.forxy.common.exceptions.ServiceException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

/**
 * Custom service exceptions mapper.
 * Maps ServiceException to jax-rs response using custom response builder.
 */
public class ResponseStatusMapper implements ExceptionMapper<ServiceException> {

    @Override
    public Response toResponse(ServiceException e) {
        return ResponseBuilder.build(Response.Status.INTERNAL_SERVER_ERROR, e.getStatusCode(), e.getMessage());
    }
}
