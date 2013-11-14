package ru.forxy.user.logic;

import ru.forxy.common.pojo.EntityPage;
import ru.forxy.user.rest.pojo.User;

/**
 * Entry point into user service business logic
 */
public interface IUserServiceFacade {

    EntityPage<User> getUsers(Integer page);

    EntityPage<User> getUsers(Integer page, Integer size);

    User getUser(User user);

    User updateUser(User user);

    User createUser(User user);

    void deleteUser(String email);
}
