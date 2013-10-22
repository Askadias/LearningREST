package ru.forxy.exceptions;

import ru.forxy.user.pojo.User;

import javax.ws.rs.core.Response;

/**
 * Throws if the specified user not found
 */
public class UserNotFoundException extends UserServiceException {

    public UserNotFoundException(User user) {
        super(Response.Status.NOT_FOUND, buildMessage(user));
    }

    private static String buildMessage(User user) {
        StringBuilder message = new StringBuilder("User");
        if (user != null && user.getEmail() != null) {
            message.append(" with email '").append(user.getEmail()).append("'");
        }
        message.append(" not found.");
        return message.toString();
    }
}
