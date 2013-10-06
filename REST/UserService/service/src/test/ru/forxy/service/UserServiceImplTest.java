package ru.forxy.service;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Assert;
import org.junit.Test;
import ru.forxy.user.pojo.User;
import ru.forxy.user.IUserService;

import java.util.List;

public class UserServiceImplTest {

    IUserService userService = new UserServiceImpl();

    @Test
    public void testAddDeleteUser() {
        userService.addUser(new User("xander@gmail.com", "xander"));
        User user = userService.login("xander@gmail.com", "xander");
        Assert.assertNotNull(user);
        Assert.assertEquals("xander@gmail.com", user.getEmail());
        userService.deleteUser("xander@gmail.com");
        user = userService.login("xander@gmail.com", "xander");
        Assert.assertNull(user);
    }

    @Test
    public void testGetAllUsers() {
        List<User> users = userService.getUsers();
        Assert.assertNotNull(users);
        Assert.assertTrue(CollectionUtils.isNotEmpty(users));
    }
}
