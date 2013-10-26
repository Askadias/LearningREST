package ru.forxy.exception;

import ru.forxy.common.exception.ServiceException;
import ru.forxy.common.service.ErrorResponseBuilder;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

/**
 * Custom user service exception mapper. Maps ServiceException to jax-rs response
 */
public class UserServiceExceptionMapper implements ExceptionMapper<ServiceException> {

    @Override
    public Response toResponse(ServiceException exception) {
        return ErrorResponseBuilder.build(Response.Status.INTERNAL_SERVER_ERROR, exception);
    }
}
