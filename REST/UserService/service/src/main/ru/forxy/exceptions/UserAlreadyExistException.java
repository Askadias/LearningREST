package ru.forxy.exceptions;

import ru.forxy.user.pojo.User;

import javax.ws.rs.core.Response;

/**
 * Throws if the specified user already exist
 */
public class UserAlreadyExistException extends UserServiceException {

    public UserAlreadyExistException(User user) {
        super(Response.Status.BAD_REQUEST, buildMessage(user));
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
