package ru.forxy.user.pojo;

import ru.forxy.common.pojo.BaseResponse;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.ws.WebServiceException;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "response")
public class UserServiceResponse extends BaseResponse {

    private List<User> users;

    public UserServiceResponse() {
    }

    public UserServiceResponse(User user) {
        if (user != null) {
            users = new ArrayList<User>(1);
            users.add(user);
        }

    }

    public UserServiceResponse(List<User> users) {
        this.users = users;
    }

    public UserServiceResponse(Exception e) {
        super(e);
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
