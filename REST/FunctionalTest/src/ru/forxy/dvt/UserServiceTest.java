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

import java.util.List;

@ContextConfiguration(locations =
        {"classpath:/ru/forxy/spring-context.xml"})
public class UserServiceTest extends AbstractJUnit4SpringContextTests {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceTest.class);

    @Autowired
    @Qualifier("userServiceClient")
    private IUserService userService;

    @Test
    public void testGetAllUsers() {
        List<User> users = userService.getUsers();
        Assert.assertTrue(CollectionUtils.isNotEmpty(users));
        LOGGER.info("Users successfully retrieved " + users);
    }

    @Test
    @Ignore
    public void addUser() {
        User user = userService.login("xander@gmail.com", "xander");
        Assert.assertNull(user);
        LOGGER.info("User(0) not yet exists: {}", user);
        User newUser = new User("xander@gmail.com", "xander");
        userService.addUser(newUser);
        user = userService.login("xander@gmail.com", "xander");
        Assert.assertNotNull(user);
        LOGGER.info("User(0) successfully added: {}", user);
        userService.deleteUser("xander@gmail.com");
        user = userService.login("xander@gmail.com", "xander");
        Assert.assertNull(user);
        LOGGER.info("User(0) successfully removed: {}", user);
    }
}
