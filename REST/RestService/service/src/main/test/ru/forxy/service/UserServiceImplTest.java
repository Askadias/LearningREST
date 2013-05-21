package ru.forxy.service;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Assert;
import org.junit.Test;
import ru.forxy.pojo.User;

import java.util.List;

public class UserServiceImplTest {

    IUserService userService = new UserServiceImpl();

    @Test
    public void testAddDeleteUser() {
        userService.addUser(new User(0, "Xander"));
        User user = userService.getUser(0);
        Assert.assertNotNull(user);
        Assert.assertEquals("Xander", user.getName());
        userService.deleteUser(0);
        user = userService.getUser(0);
        Assert.assertNull(user);
    }

    @Test
    public void testGetAllUsers() {
        List<User> users = userService.getUsers();
        Assert.assertNotNull(users);
        Assert.assertTrue(CollectionUtils.isNotEmpty(users));
    }
}
