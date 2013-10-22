package ru.forxy.exceptions;

import ru.forxy.common.exceptions.ExceptionHandler;
import ru.forxy.common.exceptions.ServiceException;
import ru.forxy.user.pojo.UserServiceResponse;

import javax.ws.rs.core.Response;

/**
 * Custom exception handler to wrap eny thrown exception
 */
public class UserServiceExceptionHandler implements ExceptionHandler<UserServiceException, UserServiceResponse> {
    @Override
    public UserServiceResponse toResponse(UserServiceException exception) {
        return new UserServiceResponse(exception);
    }
}
