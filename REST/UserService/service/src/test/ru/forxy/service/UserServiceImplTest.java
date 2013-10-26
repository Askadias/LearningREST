package ru.forxy.service;

import org.apache.commons.collections.CollectionUtils;
import org.apache.cxf.jaxrs.impl.HttpHeadersImpl;
import org.apache.cxf.jaxrs.impl.UriInfoImpl;
import org.apache.cxf.message.Message;
import org.apache.cxf.message.MessageImpl;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.forxy.common.pojo.EntityPage;
import ru.forxy.user.IUserService;
import ru.forxy.user.pojo.User;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

public class UserServiceImplTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImplTest.class);

    IUserService userService = new UserServiceImpl();

    @Test
    @Ignore
    public void testAddDeleteUser() {
        User xander = new User("xander@gmail.com", new byte[]{});
        Message m = new MessageImpl();
        UriInfo uriInfo = new UriInfoImpl(m);
        HttpHeaders headers = new HttpHeadersImpl(m);
        userService.createUser(xander, uriInfo, headers);
        Response response = userService.login(xander, uriInfo, headers);
        Assert.assertNotNull(response);
        Assert.assertNotNull(response.getEntity());
        User user = response.readEntity(User.class);
        Assert.assertEquals("xander@gmail.com", user.getEmail());
        userService.deleteUser(xander.getEmail(), uriInfo, headers);
        try {
            userService.login(xander, uriInfo, headers);
            Assert.fail();
        } catch (BadRequestException e) {
        }
    }

    @Test
    @Ignore
    public void testGetAllUsers() {
        Message m = new MessageImpl();
        UriInfo uriInfo = new UriInfoImpl(m);
        HttpHeaders headers = new HttpHeadersImpl(m);
        Response response = userService.getUsers(1, uriInfo, headers);
        Assert.assertNotNull(response);
        Assert.assertNotNull(response.getEntity());

        EntityPage<User> userPage = response.readEntity(new GenericType<EntityPage<User>>() {
        });
        Assert.assertTrue(CollectionUtils.isNotEmpty(userPage.getContent()));
    }
}
