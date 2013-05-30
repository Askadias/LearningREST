package ru.forxy.service;

import ru.forxy.pojo.User;
import ru.forxy.util.Security;

import javax.ws.rs.core.Response;
import javax.xml.ws.WebServiceException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserServiceImpl implements IUserService {

    private static Map<String, User> users = new HashMap<String, User>(3);

    static {
        users.put("alfred@gmail.com", new User("alfred@gmail.com", Security.md5("alfred")));
        users.put("bob@gmail.com", new User("bob@gmail.com", Security.md5("bob")));
        users.put("cliff@gmail.com", new User("cliff@gmail.com", Security.md5("cliff")));
        users.put("daniel@gmail.com", new User("daniel@gmail.com", Security.md5("daniel")));
        users.put("eleanor@gmail.com", new User("eleanor@gmail.com", Security.md5("eleanor")));
    }

    @Override
    public List<User> getUsers() {
        return new ArrayList<User>(users.values());
    }

    @Override
    public User login(String email, String password) {
        User user = users.get(email);
        if (user.getPassword().equals(Security.md5(password))) {
            return user;
        } else {
            return null;
        }
    }

    @Override
    public Response getBadResponse() {
        return Response.status(Response.Status.BAD_REQUEST).build();
    }

    @Override
    public void updateUser(User user) {
        if (user.getEmail() != null && user.getPassword() != null) {
            user.setPassword(Security.md5(user.getPassword()));
            users.put(user.getEmail(), user);
        } else {
            throw new WebServiceException("User's email is null");
        }
    }

    @Override
    public void addUser(User user) {
        if (user.getEmail() != null) {
            if (!users.containsKey(user.getEmail())) {
                users.put(user.getEmail(), user);
            } else {
                throw new WebServiceException("User with email " + user.getEmail() + " already exist");
            }
        } else {
            throw new WebServiceException("User's email is null");
        }
    }

    @Override
    public void deleteUser(String email) {
        users.remove(email);
    }
}
