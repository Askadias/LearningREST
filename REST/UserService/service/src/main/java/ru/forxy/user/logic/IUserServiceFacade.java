package ru.forxy.user.logic;

import ru.forxy.common.pojo.EntityPage;
import ru.forxy.user.rest.v1.pojo.User;

/**
 * Entry point into user service business logic
 */
public interface IUserServiceFacade {

    EntityPage<User> getUsers(final Integer page);

    EntityPage<User> getUsers(final Integer page, final Integer size);

    User getUser(final User user);

    User updateUser(final User user);

    User createUser(final User user);

    void deleteUser(final String email);
}
