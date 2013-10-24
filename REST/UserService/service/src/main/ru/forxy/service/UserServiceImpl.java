package ru.forxy.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import ru.forxy.common.AbstractService;
import ru.forxy.common.exceptions.ServiceException;
import ru.forxy.common.exceptions.ValidationException;
import ru.forxy.db.dao.IUserDAO;
import ru.forxy.exceptions.UserAlreadyExistException;
import ru.forxy.exceptions.UserNotFoundException;
import ru.forxy.exceptions.UserServiceException;
import ru.forxy.user.IUserService;
import ru.forxy.user.pojo.User;

import javax.ws.rs.core.Response;
import java.util.Arrays;
import java.util.List;

public class UserServiceImpl extends AbstractService implements IUserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    private static final int DEFAULT_PAGE_SIZE = 20;

    @Autowired
    IUserDAO userDAO;

    @Override
    public List<User> getUsers() throws ServiceException {
        try {
            return (List<User>) userDAO.findAll();
        } catch (Exception e) {
            throw new UserServiceException(e);
        }
    }

    @Override
    public List<User> getUsers(Integer page) throws ServiceException {
        try {
            return userDAO.findAll(new PageRequest(page, DEFAULT_PAGE_SIZE)).getContent();
        } catch (Exception e) {
            throw new UserServiceException(e);
        }
    }

    @Override
    public List<User> getUsers(Integer page, Integer size) throws ServiceException {
        try {
            return userDAO.findAll(new PageRequest(page, size)).getContent();
        } catch (Exception e) {
            throw new UserServiceException(e);
        }
    }

    @Override
    public User getUser(User queryUser) throws ServiceException {
        if (queryUser != null && queryUser.getEmail() != null) {
            return userDAO.findOne(queryUser.getEmail());
        }
        throw new UserNotFoundException(queryUser);
    }

    @Override
    public User login(User loginUser) throws ServiceException {
        User user = userDAO.findOne(loginUser.getEmail());
        if (user != null && Arrays.equals(loginUser.getPassword(), user.getPassword())) {
            return user;
        } else {
            throw new UserNotFoundException(loginUser);
        }
    }

    @Override
    public Response updateUser(User user) throws ServiceException {
        if (user.getEmail() != null && user.getPassword() != null) {
            userDAO.save(user);
            return Response.ok(user).build();
        } else {
            throw new ValidationException("User's email or password are empty");
        }
    }

    @Override
    public Response createUser(User user) throws ServiceException {
        if (user.getEmail() != null) {
            if (!userDAO.exists(user.getEmail())) {
                userDAO.save(user);
                return Response.ok(user).build();
            } else {
                throw new UserAlreadyExistException(user);
            }
        } else {
            throw new ValidationException("User's email is null");
        }
    }

    @Override
    public Response deleteUser(String email) throws ServiceException {
        if (userDAO.exists(email)) {
            userDAO.delete(email);
            return Response.ok(email).build();
        } else {
            throw new UserNotFoundException(new User(email, null));
        }
    }
}
