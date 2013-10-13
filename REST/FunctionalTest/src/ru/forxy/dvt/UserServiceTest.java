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
import ru.forxy.user.IUserService;
import ru.forxy.user.pojo.User;
import ru.forxy.user.pojo.UserServiceResponse;

import java.util.Arrays;
import java.util.List;

@ContextConfiguration(locations =
        {"classpath:/ru/forxy/spring-context.xml"})
public class UserServiceTest extends AbstractJUnit4SpringContextTests {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceTest.class);

    @Autowired
    @Qualifier("userServiceClient")
    private IUserService userService;

    @Test
    @Ignore
    public void testAddDeleteUser() {
        userService.createUser(new User("xander@gmail.com", new byte[]{}));
        UserServiceResponse response = userService.login("xander@gmail.com", new byte[]{});
        Assert.assertNotNull(response);
        Assert.assertNotNull(response.getUsers());
        Assert.assertTrue(CollectionUtils.isNotEmpty(response.getUsers()));
        LOGGER.info("User  has been successfully created: {}", response.getUsers().get(0));
        Assert.assertEquals("xander@gmail.com", response.getUsers().get(0).getEmail());
        userService.deleteUser("xander@gmail.com");
        response = userService.login("xander@gmail.com", new byte[]{});
        Assert.assertNull(response.getUsers());
        LOGGER.info("User has been successfully removed");
    }

    @Test
    public void testGetAllUsers() {
        UserServiceResponse response = userService.getUsers();
        Assert.assertNotNull(response);
        Assert.assertNotNull(response.getUsers());
        Assert.assertTrue(CollectionUtils.isNotEmpty(response.getUsers()));
        LOGGER.info("Users retrieved");
    }
}
