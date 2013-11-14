package ru.forxy.user.exceptions;

import ru.forxy.common.exceptions.support.StatusTemplate;

/**
 * User service specific exceptions
 */
public enum UserServiceExceptions {

    UserNotFound(new StatusTemplate("2000", "User with email '%s' is not found.")),
    UserAlreadyExists(new StatusTemplate("2001", "User with email '%s' already exists.")),
    EmailIsNullOrEmpty(new StatusTemplate("2003", "Requested user's email shouldn't be null or empty.")),
    EmptyLoginEmailOrPassword(new StatusTemplate("2004", "To login user's email and password should present."));

    private StatusTemplate statusTemplate;

    private UserServiceExceptions(StatusTemplate statusTemplate) {
        this.statusTemplate = statusTemplate;
    }

    public StatusTemplate getStatusTemplate() {
        return statusTemplate;
    }

}
