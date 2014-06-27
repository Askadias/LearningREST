package ru.forxy.user.logic;

import ru.forxy.common.pojo.EntityPage;
import ru.forxy.user.rest.v1.pojo.User;

import java.util.List;

/**
 * Entry point into user service business logic
 */
public interface IUserServiceFacade {

    Iterable<User> getAllUsers();

    EntityPage<User> getUsers(final Integer page);

    EntityPage<User> getUsers(final Integer page, final Integer size);

    User getUser(final String email);

    void updateUser(final User user);

    void createUser(final User user);

    void deleteUser(final String email);
}
