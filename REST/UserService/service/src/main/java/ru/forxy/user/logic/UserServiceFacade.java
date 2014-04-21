package ru.forxy.user.logic;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import ru.forxy.common.exceptions.ServiceException;
import ru.forxy.common.pojo.EntityPage;
import ru.forxy.user.db.dao.IUserDAO;
import ru.forxy.user.exceptions.UserServiceExceptions;
import ru.forxy.user.rest.pojo.User;

/**
 * Implementation class for UserService business logic
 */
public class UserServiceFacade implements IUserServiceFacade {

    private static final int DEFAULT_PAGE_SIZE = 10;

    private IUserDAO userDAO;

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
    public User getUser(final User user) {
        if (user != null && user.getEmail() != null) {
            return userDAO.findOne(user.getEmail());
        }
        throw new ServiceException(UserServiceExceptions.EmailIsNullOrEmpty.getStatusTemplate());
    }

    @Override
    public User updateUser(final User user) {
        if (user.getEmail() != null && user.getPassword() != null) {
            userDAO.save(user);
            return user;
        } else {
            throw new ServiceException(UserServiceExceptions.EmptyLoginEmailOrPassword.getStatusTemplate());
        }
    }

    @Override
    public User createUser(final User user) {
        if (user.getEmail() != null) {
            if (!userDAO.exists(user.getEmail())) {
                userDAO.save(user);
                return user;
            } else {
                throw new ServiceException(UserServiceExceptions.UserAlreadyExists.getStatusTemplate(),
                        user.getEmail());
            }
        } else {
            throw new ServiceException(UserServiceExceptions.EmailIsNullOrEmpty.getStatusTemplate());
        }
    }

    @Override
    public void deleteUser(final String email) {
        if (userDAO.exists(email)) {
            userDAO.delete(email);
        } else {
            throw new ServiceException(UserServiceExceptions.UserNotFound.getStatusTemplate(), email);
        }
    }

    public void setUserDAO(final IUserDAO userDAO) {
        this.userDAO = userDAO;
    }
}
