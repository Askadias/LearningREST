package ru.forxy.user.test.dvt;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.forxy.common.pojo.EntityPage;
import ru.forxy.user.rest.v1.IUserServiceClient;
import ru.forxy.user.rest.v1.pojo.User;
import ru.forxy.user.test.BaseUserServiceTest;

import java.util.UUID;

/**
 * Test success path for UserService client
 */
public class UserServiceClientSuccessPathTest extends BaseUserServiceTest {

    @Autowired
    IUserServiceClient userServiceClient;

    @Test
    public void testGetAllUsers() {
        Assert.assertNotNull(userServiceClient);
        String transactionGUID = UUID.randomUUID().toString();
        EntityPage<User> userPage = userServiceClient.getUsers(transactionGUID, 0);
        Assert.assertNotNull(userPage);
    }
}
