package ru.forxy.exceptions;

import ru.forxy.common.service.ErrorResponseBuilder;
import ru.forxy.user.pojo.User;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.core.Response;

/**
 * Throws if the specified user already exist
 */
public class UserAlreadyExistException extends BadRequestException {

    public UserAlreadyExistException(User user) {
        super(ErrorResponseBuilder.build(Response.Status.BAD_REQUEST, buildMessage(user)));
    }

    private static String buildMessage(User user) {
        StringBuilder message = new StringBuilder("User");
        if (user != null && user.getEmail() != null) {
            message.append(" with email '").append(user.getEmail()).append("'");
        }
        message.append(" already exist.");
        return message.toString();
    }
}
