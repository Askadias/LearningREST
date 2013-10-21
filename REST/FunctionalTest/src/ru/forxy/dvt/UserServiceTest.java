package ru.forxy.dvt;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import ru.forxy.BaseSpringContextTest;
import ru.forxy.user.IUserService;
import ru.forxy.user.pojo.User;
import ru.forxy.user.pojo.UserServiceResponse;

import java.util.Arrays;
import java.util.List;

public class UserServiceTest extends BaseSpringContextTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceTest.class);

    @Autowired
    @Qualifier("userServiceClient")
    private IUserService userService;

    @Test
    public void testAddDeleteUser() {
        User xander = new User("xander@gmail.com", new byte[]{});
        userService.createUser(xander);
        UserServiceResponse response = userService.login(xander);
        Assert.assertNotNull(response);
        Assert.assertNotNull(response.getUsers());
        Assert.assertTrue(CollectionUtils.isNotEmpty(response.getUsers()));
        LOGGER.info("User  has been successfully created: {}", response.getUsers().get(0));
        Assert.assertEquals("xander@gmail.com", response.getUsers().get(0).getEmail());
        userService.deleteUser(xander.getEmail());
        response = userService.login(xander);
        Assert.assertNull(response.getUsers());
        LOGGER.info("User has been successfully removed");
    }

    @Test
    public void testGetUserPage() {
        UserServiceResponse response = userService.getUsers(1);
        Assert.assertNotNull(response);
        Assert.assertNotNull(response.getUsers());
        Assert.assertTrue(CollectionUtils.isNotEmpty(response.getUsers()));
    }

    @Test
    public void testGetUser() {
        User user = new User("Rachel_Kingson@gmail.com", null);
        UserServiceResponse response = userService.getUser(user);
        Assert.assertNotNull(response);
        Assert.assertNotNull(response.getUsers());
        Assert.assertTrue(CollectionUtils.isNotEmpty(response.getUsers()));
    }

    @Test
    @Ignore
    public void testGetAllUsers() {
        UserServiceResponse response = userService.getUsers();
        Assert.assertNotNull(response);
        Assert.assertNotNull(response.getUsers());
        Assert.assertTrue(CollectionUtils.isNotEmpty(response.getUsers()));
        LOGGER.info("Users retrieved");
    }
}
