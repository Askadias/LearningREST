package ru.forxy.service;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Assert;
import org.junit.Test;
import ru.forxy.user.pojo.User;
import ru.forxy.user.IUserService;
import ru.forxy.user.pojo.UserServiceResponse;

import java.util.List;

public class UserServiceImplTest {

    IUserService userService = new UserServiceImpl();

    @Test
    public void testAddDeleteUser() {
        userService.createUser(new User("xander@gmail.com", new byte[]{}));
        UserServiceResponse response = userService.login("xander@gmail.com", new byte[]{});
        Assert.assertNotNull(response);
        Assert.assertNotNull(response.getUsers());
        Assert.assertTrue(CollectionUtils.isNotEmpty(response.getUsers()));
        Assert.assertEquals("xander@gmail.com", response.getUsers().get(0).getEmail());
        userService.deleteUser("xander@gmail.com");
        response = userService.login("xander@gmail.com", new byte[]{});
        Assert.assertNull(response.getUsers());
    }

    @Test
    public void testGetAllUsers() {
        UserServiceResponse response = userService.getUsers();
        Assert.assertNotNull(response);
        Assert.assertNotNull(response.getUsers());
        Assert.assertTrue(CollectionUtils.isNotEmpty(response.getUsers()));
    }
}
