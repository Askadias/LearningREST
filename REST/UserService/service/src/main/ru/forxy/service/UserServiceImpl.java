package ru.forxy.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import ru.forxy.common.pojo.ResponseMessage;
import ru.forxy.db.dao.IUserDAO;
import ru.forxy.user.IUserService;
import ru.forxy.user.pojo.User;
import ru.forxy.user.pojo.UserServiceResponse;

import javax.ws.rs.NotFoundException;
import javax.xml.ws.WebServiceException;
import java.util.*;

public class UserServiceImpl implements IUserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    IUserDAO userDAO;

    private static Map<String, User> users = new HashMap<String, User>(3);

    static {
        users.put("alfred@gmail.com", new User("alfred@gmail.com", new byte[]{}));
        users.put("bob@gmail.com", new User("bob@gmail.com", new byte[]{}));
        users.put("cliff@gmail.com", new User("cliff@gmail.com", new byte[]{}));
        users.put("daniel@gmail.com", new User("daniel@gmail.com", new byte[]{}));
        users.put("eleanor@gmail.com", new User("eleanor@gmail.com", new byte[]{}));
    }

    @Override
    public UserServiceResponse getUsers() {
        return new UserServiceResponse((List<User>) userDAO.findAll());
    }

    @Override
    public UserServiceResponse login(User login) {
        User user = userDAO.findOne(login.getEmail());
        if (user != null && Arrays.equals(login.getPassword(), user.getPassword())) {
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
