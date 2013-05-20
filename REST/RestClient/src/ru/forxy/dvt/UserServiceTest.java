package ru.forxy.dvt;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import ru.forxy.service.IUserService;
import ru.forxy.service.pojo.User;

import java.util.List;

@ContextConfiguration(locations =
        {"classpath:ru/forxy/spring-context.xml"})
public class UserServiceTest extends AbstractJUnit4SpringContextTests {

    @Autowired
    @Qualifier("userServiceClient")
    private IUserService userService;

    @Test
    public void testGetAllUsers() {
        List<User> users = userService.getUsers();
        Assert.assertTrue(CollectionUtils.isNotEmpty(users));
    }

    @Test
    public void addUser() {
        User user = userService.getUser(0);
        Assert.assertNull(user);
        User newUser = new User(0, "Xander");
        userService.addUser(newUser);
        user = userService.getUser(0);
        Assert.assertNotNull(user);
        userService.deleteUser(0);
        user = userService.getUser(0);
        Assert.assertNull(user);
    }
}
