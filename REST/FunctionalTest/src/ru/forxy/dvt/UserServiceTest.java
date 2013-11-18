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
import ru.forxy.BaseSpringContextTest;
import ru.forxy.common.pojo.EntityPage;
import ru.forxy.common.service.ISystemStatusService;
import ru.forxy.user.rest.IUserService;
import ru.forxy.user.rest.pojo.User;

import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

public class UserServiceTest extends BaseSpringContextTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceTest.class);

    @Autowired
    private IUserService userService;

    @Autowired
    private ISystemStatusService userSystemStatus;

    @Test
    public void testAddDeleteUser() {
        Message m = new MessageImpl();
        UriInfo uriInfo = new UriInfoImpl(m);
        HttpHeaders headers = new HttpHeadersImpl(m);
        User xander = new User("xander@gmail.com", new byte[]{});
        Response response = userService.createUser(xander, uriInfo, headers);
        Assert.assertNotNull(response);
        response = userService.login(xander, uriInfo, headers);
        Assert.assertNotNull(response);
        User user = response.readEntity(User.class);
        Assert.assertNotNull(user);
        LOGGER.info("User  has been successfully created: {}", user);
        Assert.assertEquals("xander@gmail.com", user.getEmail());
        response = userService.deleteUser(xander.getEmail(), uriInfo, headers);
        Assert.assertNotNull(response);
        response = userService.getUser(xander, uriInfo, headers);
        Object entity = response.getEntity();
        Assert.assertNotNull(entity);
        Assert.assertEquals(404, response.getStatus());
        LOGGER.info("User has been successfully removed");
    }

    @Test
    public void testGetUserPage() {
        Message m = new MessageImpl();
        UriInfo uriInfo = new UriInfoImpl(m);
        HttpHeaders headers = new HttpHeadersImpl(m);
        Response response = userService.getUsers(0, uriInfo, headers);
        Assert.assertNotNull(response);
        EntityPage<User> userPage = response.readEntity(new GenericType<EntityPage<User>>() {
        });
        Assert.assertNotNull(userPage);
        Assert.assertTrue(CollectionUtils.isNotEmpty(userPage.getContent()));
    }

    @Test
    public void testSystemStatus() {
        Message m = new MessageImpl();
        UriInfo uriInfo = new UriInfoImpl(m);
        HttpHeaders headers = new HttpHeadersImpl(m);
        Response response = userSystemStatus.getSystemStatus(uriInfo, headers);
        Assert.assertNotNull(response);
        Assert.assertEquals(response.getStatus(), Response.Status.OK.getStatusCode());

    }
}
