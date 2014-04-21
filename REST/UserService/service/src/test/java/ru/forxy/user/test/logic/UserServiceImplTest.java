package ru.forxy.user.test.logic;

import org.apache.commons.collections.CollectionUtils;
import org.apache.cxf.jaxrs.impl.HttpHeadersImpl;
import org.apache.cxf.jaxrs.impl.UriInfoImpl;
import org.apache.cxf.message.Message;
import org.apache.cxf.message.MessageImpl;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import ru.forxy.common.pojo.EntityPage;
import ru.forxy.user.db.dao.IUserDAO;
import ru.forxy.user.rest.IUserService;
import ru.forxy.user.rest.pojo.User;
import ru.forxy.user.test.BaseUserServiceTest;

import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.ArrayList;
import java.util.List;

public class UserServiceImplTest extends BaseUserServiceTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImplTest.class);

    private static final String TEST_USER_EMAIL = "xander@gmail.com";

    @Autowired
    IUserService userService;

    @Autowired
    @Qualifier("ru.forxy.user.db.dao.UserDAO.mongo")
    IUserDAO userDAOMock;

    @Test
    public void testAddDeleteUser() {
        User testUser = new User(TEST_USER_EMAIL, new byte[]{});
        Message m = new MessageImpl();
        UriInfo uriInfo = new UriInfoImpl(m);
        HttpHeaders headers = new HttpHeadersImpl(m);

        // Mock setup
        EasyMock.expect(userDAOMock.exists(TEST_USER_EMAIL)).andReturn(false);
        EasyMock.expect(userDAOMock.save(testUser)).andReturn(testUser);
        EasyMock.expect(userDAOMock.findOne(TEST_USER_EMAIL)).andReturn(testUser);
        EasyMock.expect(userDAOMock.exists(TEST_USER_EMAIL)).andReturn(true);
        EasyMock.expect(userDAOMock.findOne(TEST_USER_EMAIL)).andReturn(null);
        EasyMock.replay(userDAOMock);

        // Create user
        userService.createUser(testUser, uriInfo, headers);
        // Login user
        Response response = userService.login(testUser, uriInfo, headers);
        Assert.assertNotNull(response);
        Assert.assertNotNull(response.getEntity());
        User user = response.readEntity(User.class);
        Assert.assertEquals(TEST_USER_EMAIL, user.getEmail());

        // Remove user
        userService.deleteUser(testUser.getEmail(), uriInfo, headers);

        // Check user has been removed
        try {
            userService.login(testUser, uriInfo, headers);
            Assert.fail();
        } catch (NotAuthorizedException e) {
            LOGGER.info("User {} has been successfully deleted.", TEST_USER_EMAIL);
        } finally {
            EasyMock.reset(userDAOMock);
        }
    }

    @Test
    public void testGetAllUsers() {
        Message m = new MessageImpl();
        UriInfo uriInfo = new UriInfoImpl(m);
        HttpHeaders headers = new HttpHeadersImpl(m);
        List<User> users = new ArrayList<User>();
        users.add(new User(TEST_USER_EMAIL, null));
        EasyMock.expect(userDAOMock.findAll(EasyMock.anyObject(Pageable.class))).andReturn(new PageImpl<User>(users));
        EasyMock.replay(userDAOMock);
        Response response = userService.getUsers(1, uriInfo, headers);
        EasyMock.reset(userDAOMock);
        Assert.assertNotNull(response);
        Assert.assertNotNull(response.getEntity());

        EntityPage<User> userPage = response.readEntity(new GenericType<EntityPage<User>>() {});
        Assert.assertTrue(CollectionUtils.isNotEmpty(userPage.getContent()));
    }
}
