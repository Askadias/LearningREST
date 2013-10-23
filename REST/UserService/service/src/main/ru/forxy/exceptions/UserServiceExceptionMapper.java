package ru.forxy.exceptions;

import ru.forxy.common.exceptions.ServiceException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

/**
 * Custom user service exception mapper. Maps ServiceException to jax-rs response
 */
public class UserServiceExceptionMapper implements ExceptionMapper<ServiceException> {

    @Override
    public Response toResponse(ServiceException exception) {
        return Response.status(exception.getStatus()).build();
    }
}
