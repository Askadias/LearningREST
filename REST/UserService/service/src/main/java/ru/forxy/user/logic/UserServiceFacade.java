package ru.forxy.user.logic;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import ru.forxy.common.exceptions.ServiceException;
import ru.forxy.common.pojo.EntityPage;
import ru.forxy.user.db.dao.IUserDAO;
import ru.forxy.user.exceptions.UserServiceEventLogId;
import ru.forxy.user.rest.v1.pojo.User;

import java.util.LinkedList;
import java.util.List;

/**
 * Implementation class for UserService business logic
 */
public class UserServiceFacade implements IUserServiceFacade {

    private static final int DEFAULT_PAGE_SIZE = 10;

    private IUserDAO userDAO;

    @Override
    public List<User> getAllUsers() {
        List<User> allUsers = new LinkedList<User>();
        for (User user : userDAO.findAll()) {
            allUsers.add(user);
        }
        return allUsers;
    }

    @Override
    public EntityPage<User> getUsers(final Integer page) {
        return getUsers(page, null);
    }

    @Override
    public EntityPage<User> getUsers(final Integer page, final Integer size) {
        final Page<User> p = userDAO.findAll(new PageRequest(page, size == null ? DEFAULT_PAGE_SIZE : size));
        return new EntityPage<User>(p.getContent(), p.getSize(), p.getNumber(), p.getTotalElements());
    }

    @Override
    public User getUser(final String email) {
        User user = userDAO.findOne(email);
        if (user == null) {
            throw new ServiceException(UserServiceEventLogId.UserNotFound, email);
        }
        return user;
    }

    @Override
    public void updateUser(final User user) {
        if (userDAO.exists(user.getEmail())) {
            userDAO.save(user);
        } else {
            throw new ServiceException(UserServiceEventLogId.UserNotFound, user.getEmail());
        }
    }

    @Override
    public void createUser(final User user) {
        if (!userDAO.exists(user.getEmail())) {
            userDAO.save(user);
        } else {
            throw new ServiceException(UserServiceEventLogId.UserAlreadyExists, user.getEmail());
        }
    }

    @Override
    public void deleteUser(final String email) {
        if (userDAO.exists(email)) {
            userDAO.delete(email);
        } else {
            throw new ServiceException(UserServiceEventLogId.UserNotFound, email);
        }
    }

    public void setUserDAO(final IUserDAO userDAO) {
        this.userDAO = userDAO;
    }
}
