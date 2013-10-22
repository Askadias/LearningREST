package ru.forxy.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import ru.forxy.common.exceptions.ServiceException;
import ru.forxy.common.exceptions.ValidationException;
import ru.forxy.common.pojo.ResponseMessage;
import ru.forxy.db.dao.IUserDAO;
import ru.forxy.exceptions.UserAlreadyExistException;
import ru.forxy.exceptions.UserNotFoundException;
import ru.forxy.exceptions.UserServiceException;
import ru.forxy.user.IUserService;
import ru.forxy.user.pojo.User;
import ru.forxy.user.pojo.UserServiceResponse;

import java.util.Arrays;
import java.util.List;

public class UserServiceImpl implements IUserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    private static final int DEFAULT_PAGE_SIZE = 20;

    @Autowired
    IUserDAO userDAO;

    @Override
    public UserServiceResponse getUsers() throws ServiceException {
        try {
            return new UserServiceResponse((List<User>) userDAO.findAll());
        } catch (Exception e) {
            throw new UserServiceException(e);
        }
    }

    @Override
    public UserServiceResponse getUsers(Integer page) throws ServiceException {
        try {
            return new UserServiceResponse(userDAO.findAll(new PageRequest(page, DEFAULT_PAGE_SIZE)).getContent());
        } catch (Exception e) {
            throw new UserServiceException(e);
        }
    }

    @Override
    public UserServiceResponse getUsers(Integer page, Integer size) throws ServiceException {
        try {
            return new UserServiceResponse(userDAO.findAll(new PageRequest(page, size)).getContent());
        } catch (Exception e) {
            throw new UserServiceException(e);
        }
    }

    @Override
    public UserServiceResponse getUser(User queryUser) throws ServiceException {
        try {
            if (queryUser != null && queryUser.getEmail() != null) {
                User user = userDAO.findOne(queryUser.getEmail());
                if (user != null) {
                    return new UserServiceResponse(user);
                }
            }
        } catch (Exception e) {
            throw new UserServiceException(e);
        }
        throw new UserNotFoundException(queryUser);
    }

    @Override
    public UserServiceResponse login(User loginUser) throws ServiceException {
        try {
            User user = userDAO.findOne(loginUser.getEmail());
            if (user != null && Arrays.equals(loginUser.getPassword(), user.getPassword())) {
                return new UserServiceResponse(user);
            } else {
                throw new UserNotFoundException(loginUser);
            }
        } catch (ServiceException use) {
            throw use;
        } catch (Exception e) {
            throw new UserServiceException(e);
        }
    }

    @Override
    public UserServiceResponse updateUser(User user) throws ServiceException {
        try {
            if (user.getEmail() != null && user.getPassword() != null) {
                userDAO.save(user);
                final UserServiceResponse response = new UserServiceResponse();
                response.addMessage(new ResponseMessage("User " + user.getEmail() + " successfully updated."));
                return response;
            } else {
                throw new ValidationException("User's email or password are empty");
            }
        } catch (ServiceException use) {
            throw use;
        } catch (Exception e) {
            throw new UserServiceException(e);
        }
    }

    @Override
    public UserServiceResponse createUser(User user) throws ServiceException {
        try {
            if (user.getEmail() != null) {
                if (!userDAO.exists(user.getEmail())) {
                    userDAO.save(user);
                    final UserServiceResponse response = new UserServiceResponse();
                    response.addMessage(new ResponseMessage("User " + user.getEmail() + " successfully created."));
                    return response;
                } else {
                    throw new UserAlreadyExistException(user);
                }
            } else {
                throw new ValidationException("User's email is null");
            }
        } catch (ServiceException use) {
            throw use;
        } catch (Exception e) {
            throw new UserServiceException(e);
        }
    }

    @Override
    public UserServiceResponse deleteUser(String email) throws ServiceException {
        try {
            if (userDAO.exists(email)) {
                userDAO.delete(email);
                final UserServiceResponse response = new UserServiceResponse();
                response.addMessage(new ResponseMessage("User " + email + " successfully deleted."));
                return response;
            } else {
                throw new UserNotFoundException(new User(email, null));
            }
        } catch (Exception e) {
            throw new UserServiceException(e);
        }
    }
}
