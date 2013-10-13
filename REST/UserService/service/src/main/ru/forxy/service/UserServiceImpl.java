package ru.forxy.service;

import ru.forxy.common.pojo.ResponseMessage;
import ru.forxy.user.IUserService;
import ru.forxy.user.pojo.User;
import ru.forxy.user.pojo.UserServiceResponse;

import javax.xml.ws.WebServiceException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UserServiceImpl implements IUserService {

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
        return new UserServiceResponse(new ArrayList<User>(users.values()));
    }

    @Override
    public UserServiceResponse login(String email, byte[] password) {
        return new UserServiceResponse(users.get(email));
    }

    @Override
    public UserServiceResponse updateUser(User user) {
        if (user.getEmail() != null && user.getPassword() != null) {
            user.setPassword(user.getPassword());
            users.put(user.getEmail(), user);
            final UserServiceResponse response = new UserServiceResponse();
            response.addMessage(new ResponseMessage("User " + user.getEmail() + " successfully updated."));
            return response;
        } else {
            return new UserServiceResponse(new WebServiceException("User's email is null"));
        }
    }

    @Override
    public UserServiceResponse createUser(User user) {
        if (user.getEmail() != null) {
            if (!users.containsKey(user.getEmail())) {
                users.put(user.getEmail(), user);
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
        users.remove(email);
        final UserServiceResponse response = new UserServiceResponse();
        response.addMessage(new ResponseMessage("User " + email + " successfully deleted."));
        return response;
    }

    @Override
    public UserServiceResponse deleteUser(User user) {
        users.remove(user.getEmail());
        final UserServiceResponse response = new UserServiceResponse();
        response.addMessage(new ResponseMessage("User " + user.getEmail() + " successfully deleted."));
        return response;
    }
}
