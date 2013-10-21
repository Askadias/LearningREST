package ru.forxy.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import ru.forxy.common.pojo.ResponseMessage;
import ru.forxy.db.dao.IUserDAO;
import ru.forxy.user.IUserService;
import ru.forxy.user.pojo.User;
import ru.forxy.user.pojo.UserServiceResponse;

import javax.ws.rs.NotFoundException;
import javax.xml.ws.WebServiceException;
import java.util.Arrays;
import java.util.List;

public class UserServiceImpl implements IUserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    private static final int DEFAULT_PAGE_SIZE = 20;

    @Autowired
    IUserDAO userDAO;

    @Override
    public UserServiceResponse getUsers() {
        return new UserServiceResponse((List<User>) userDAO.findAll());
    }

    @Override
    public UserServiceResponse getUsers(Integer page) {
        return new UserServiceResponse(userDAO.findAll(new PageRequest(page, DEFAULT_PAGE_SIZE)).getContent());
    }

    @Override
    public UserServiceResponse getUsers(Integer page, Integer size) {
        return new UserServiceResponse(userDAO.findAll(new PageRequest(page, size)).getContent());
    }

    @Override
    public UserServiceResponse getUser(User queryUser) {
        if (queryUser != null && queryUser.getEmail() != null) {
            User user = userDAO.findOne(queryUser.getEmail());
            if (user != null) {
                return new UserServiceResponse(user);
            }
        }
        return new UserServiceResponse(new NotFoundException());
    }

    @Override
    public UserServiceResponse login(User loginUser) {
        User user = userDAO.findOne(loginUser.getEmail());
        if (user != null && Arrays.equals(loginUser.getPassword(), user.getPassword())) {
            return new UserServiceResponse(user);
        } else {
            return new UserServiceResponse(new NotFoundException());
        }
    }

    @Override
    public UserServiceResponse updateUser(User user) {
        if (user.getEmail() != null && user.getPassword() != null) {
            userDAO.save(user);
            final UserServiceResponse response = new UserServiceResponse();
            response.addMessage(new ResponseMessage("User " + user.getEmail() + " successfully updated."));
            return response;
        } else {
            return new UserServiceResponse(new WebServiceException("User's email or password are empty"));
        }
    }

    @Override
    public UserServiceResponse createUser(User user) {
        if (user.getEmail() != null) {
            if (!userDAO.exists(user.getEmail())) {
                userDAO.save(user);
                final UserServiceResponse response = new UserServiceResponse();
                response.addMessage(new ResponseMessage("User " + user.getEmail() + " successfully created."));
                return response;
            } else {
                throw new WebServiceException("User with email " + user.getEmail() + " already exist");
            }
        } else {
            return new UserServiceResponse(new WebServiceException("User's email is null"));
        }
    }

    @Override
    public UserServiceResponse deleteUser(String email) {
        userDAO.delete(email);
        final UserServiceResponse response = new UserServiceResponse();
        response.addMessage(new ResponseMessage("User " + email + " successfully deleted."));
        return response;
    }
}
