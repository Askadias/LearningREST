package ru.forxy.exception;

import ru.forxy.common.service.ErrorResponseBuilder;
import ru.forxy.user.pojo.User;

import javax.ws.rs.NotFoundException;
import static javax.ws.rs.core.Response.Status;

/**
 * Throws if the specified user not found
 */
public class UserNotFoundException extends NotFoundException {

    public UserNotFoundException(User user) {
        super (ErrorResponseBuilder.build(Status.NOT_FOUND, buildMessage(user)));
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
