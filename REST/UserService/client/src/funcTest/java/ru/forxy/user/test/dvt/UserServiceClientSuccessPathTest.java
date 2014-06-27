package ru.forxy.user.test.dvt;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.forxy.common.exceptions.ClientException;
import ru.forxy.common.pojo.EntityPage;
import ru.forxy.common.pojo.StatusEntity;
import ru.forxy.user.rest.v1.IUserServiceClient;
import ru.forxy.user.rest.v1.pojo.User;
import ru.forxy.user.test.BaseUserServiceTest;

import java.util.Date;
import java.util.GregorianCalendar;
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

    @Test
    public void createUpdateDeleteUser() {
        // prepare test data
        String transactionGUID = UUID.randomUUID().toString();
        User expectedUser = new User("kast.askadias@mail.ru", new byte[]{0,0,0});
        expectedUser.setFirstName("Kast");
        expectedUser.setLastName("Askadias");
        expectedUser.setGender('m');
        expectedUser.setLogin("Kast");
        expectedUser.setBirthDate(new GregorianCalendar(1987, 1, 3).getTime());
        StatusEntity status;
        try {
            // creating user
            status = userServiceClient.createUser(transactionGUID, expectedUser);
            Assert.assertEquals("200", status.getCode());
            User actualUser = userServiceClient.getUser(transactionGUID, expectedUser.getEmail());
            Assert.assertEquals(expectedUser.getEmail(), actualUser.getEmail());

            // updating user
            expectedUser.setFirstName("Xander");
            status = userServiceClient.updateUser(transactionGUID, expectedUser);
            Assert.assertEquals("200", status.getCode());
            actualUser = userServiceClient.getUser(transactionGUID, expectedUser.getEmail());
            Assert.assertEquals(expectedUser.getFirstName(), actualUser.getFirstName());
        } catch (Throwable th) {
            th.printStackTrace();
        } finally {
            // deleting user
            status = userServiceClient.deleteUser(transactionGUID, expectedUser.getEmail());
            Assert.assertEquals("200", status.getCode());
            try {
                userServiceClient.getUser(transactionGUID, expectedUser.getEmail());
                Assert.fail();
            } catch (ClientException e) {
                Assert.assertNotNull(e.getErrorEntity());
                Assert.assertEquals("404", e.getErrorEntity().getCode());
            }
        }
    }
}
