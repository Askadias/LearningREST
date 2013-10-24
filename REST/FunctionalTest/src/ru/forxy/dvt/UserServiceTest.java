package ru.forxy.dvt;

import org.apache.commons.collections.CollectionUtils;
import org.apache.cxf.jaxrs.impl.HttpHeadersImpl;
import org.apache.cxf.jaxrs.impl.UriInfoImpl;
import org.apache.cxf.message.Message;
import org.apache.cxf.message.MessageImpl;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import ru.forxy.BaseSpringContextTest;
import ru.forxy.user.IUserService;
import ru.forxy.user.pojo.User;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.List;

public class UserServiceTest extends BaseSpringContextTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceTest.class);

    @Autowired
    @Qualifier("userServiceClient")
    private IUserService userService;

    @Test
    public void testAddDeleteUser() {
        Message m = new MessageImpl();
        UriInfo uriInfo = new UriInfoImpl(m);
        HttpHeaders headers = new HttpHeadersImpl(m);
        User xander = new User("xander@gmail.com", new byte[]{});
        userService.createUser(xander, uriInfo, headers);
        Response response = userService.login(xander, uriInfo, headers);
        Assert.assertNotNull(response);
        User user = response.readEntity(User.class);
        Assert.assertNotNull(user);
        LOGGER.info("User  has been successfully created: {}", user);
        Assert.assertEquals("xander@gmail.com", user.getEmail());
        userService.deleteUser(xander.getEmail(), uriInfo, headers);
        response = userService.login(xander, uriInfo, headers);
        Object entity = response.getEntity();
        Assert.assertNotNull(entity);
        Assert.assertEquals(response.getStatus(), 500);
        LOGGER.info("User has been successfully removed");
    }

    @Test
    public void testGetUserPage() {
        Message m = new MessageImpl();
        UriInfo uriInfo = new UriInfoImpl(m);
        HttpHeaders headers = new HttpHeadersImpl(m);
        Response response = userService.getUsers(1, uriInfo, headers);
        Assert.assertNotNull(response);
        List<User> users = response.readEntity(List.class);
        Assert.assertNotNull(users);
        Assert.assertTrue(CollectionUtils.isNotEmpty(users));
    }

    @Test
    public void testGetUser() {
        Message m = new MessageImpl();
        UriInfo uriInfo = new UriInfoImpl(m);
        HttpHeaders headers = new HttpHeadersImpl(m);
        User user = new User("Rachel_Kingson@gmail.com", null);
        Response response = userService.getUser(user, uriInfo, headers);
        Assert.assertNotNull(response);
        user = response.readEntity(User.class);
        Assert.assertNotNull(user);
    }
}
