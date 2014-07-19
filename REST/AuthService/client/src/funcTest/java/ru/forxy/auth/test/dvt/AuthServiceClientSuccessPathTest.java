package ru.forxy.auth.test.dvt;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.forxy.auth.pojo.Client;
import ru.forxy.auth.rest.v1.IAuthServiceClient;
import ru.forxy.auth.test.BaseAuthServiceTest;
import ru.forxy.common.exceptions.ClientException;
import ru.forxy.common.pojo.EntityPage;
import ru.forxy.common.pojo.StatusEntity;

import java.util.UUID;

/**
 * Test success path for AuthService client
 */
public class AuthServiceClientSuccessPathTest extends BaseAuthServiceTest {

    @Autowired
    IAuthServiceClient authServiceClient;

    @Test
    public void testGetAllAuths() {
        Assert.assertNotNull(authServiceClient);
        String transactionGUID = UUID.randomUUID().toString();
        EntityPage<Client> authPage = authServiceClient.getClients(transactionGUID, 1);
        Assert.assertNotNull(authPage);
    }

    @Test
    public void createUpdateDeleteAuth() {
        // prepare test data
        String transactionGUID = UUID.randomUUID().toString();
        Client expectedClient = new Client();
        expectedClient.setApplicationName("Test");
        expectedClient.setClientSecret("secret");
        expectedClient.setApplicationDescription("Description");
        StatusEntity status;
        try {
            // creating auth
            status = authServiceClient.createClient(transactionGUID, expectedClient);
            Assert.assertEquals("200", status.getCode());
            Client actualClient = authServiceClient.getClient(transactionGUID, expectedClient.getApplicationName());
            Assert.assertEquals(expectedClient.getApplicationName(), actualClient.getApplicationName());

            // updating auth
            expectedClient.setApplicationName("Test2");
            status = authServiceClient.updateClient(transactionGUID, expectedClient);
            Assert.assertEquals("200", status.getCode());
            actualClient = authServiceClient.getClient(transactionGUID, expectedClient.getApplicationName());
            Assert.assertEquals(expectedClient.getApplicationName(), actualClient.getApplicationName());
        } catch (Throwable th) {
            th.printStackTrace();
        } finally {
            // deleting auth
            status = authServiceClient.deleteClient(transactionGUID, expectedClient.getApplicationName());
            Assert.assertEquals("200", status.getCode());
            try {
                authServiceClient.getClient(transactionGUID, expectedClient.getApplicationName());
                Assert.fail();
            } catch (ClientException e) {
                Assert.assertNotNull(e.getErrorEntity());
                Assert.assertEquals("404", e.getErrorEntity().getCode());
            }
        }
    }
}
