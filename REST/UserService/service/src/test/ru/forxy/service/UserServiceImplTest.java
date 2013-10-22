package ru.forxy.service;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.forxy.common.exceptions.ServiceException;
import ru.forxy.user.IUserService;
import ru.forxy.user.pojo.User;
import ru.forxy.user.pojo.UserServiceResponse;

public class UserServiceImplTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImplTest.class);

    IUserService userService = new UserServiceImpl();

    @Test
    @Ignore
    public void testAddDeleteUser() throws ServiceException {
        User xander = new User("xander@gmail.com", new byte[]{});
        userService.createUser(xander);
        UserServiceResponse response = userService.login(xander);
        Assert.assertNotNull(response);
        Assert.assertNotNull(response.getUsers());
        Assert.assertTrue(CollectionUtils.isNotEmpty(response.getUsers()));
        Assert.assertEquals("xander@gmail.com", response.getUsers().get(0).getEmail());
        userService.deleteUser(xander.getEmail());
        try {
            userService.login(xander);
        } catch (ServiceException e) {
            Assert.assertEquals(e.getMessage(), "User with email 'xander@gmail.com' not found");
        }
        Assert.fail();
    }

    @Test
    @Ignore
    public void testGetAllUsers() throws ServiceException {
        UserServiceResponse response = userService.getUsers();
        Assert.assertNotNull(response);
        Assert.assertNotNull(response.getUsers());
        Assert.assertTrue(CollectionUtils.isNotEmpty(response.getUsers()));
    }
}
