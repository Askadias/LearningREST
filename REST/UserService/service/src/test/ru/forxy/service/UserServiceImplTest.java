package ru.forxy.service;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import ru.forxy.user.IUserService;
import ru.forxy.user.pojo.User;
import ru.forxy.user.pojo.UserServiceResponse;

public class UserServiceImplTest {

    IUserService userService = new UserServiceImpl();

    @Test
    @Ignore
    public void testAddDeleteUser() {
        User xander = new User("xander@gmail.com", new byte[]{});
        userService.createUser(xander);
        UserServiceResponse response = userService.login(xander);
        Assert.assertNotNull(response);
        Assert.assertNotNull(response.getUsers());
        Assert.assertTrue(CollectionUtils.isNotEmpty(response.getUsers()));
        Assert.assertEquals("xander@gmail.com", response.getUsers().get(0).getEmail());
        userService.deleteUser(xander.getEmail());
        response = userService.login(xander);
        Assert.assertNull(response.getUsers());
    }

    @Test
    @Ignore
    public void testGetAllUsers() {
        UserServiceResponse response = userService.getUsers();
        Assert.assertNotNull(response);
        Assert.assertNotNull(response.getUsers());
        Assert.assertTrue(CollectionUtils.isNotEmpty(response.getUsers()));
    }
}
