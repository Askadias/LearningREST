package main.java.ru.forxy.dvt;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import ru.forxy.pojo.User;
import ru.forxy.service.ICryptoService;

import java.util.Arrays;
import java.util.List;

@ContextConfiguration(locations =
        {"classpath:main/java/ru/forxy/spring-context.xml"})
public class CryptoServiceTest extends AbstractJUnit4SpringContextTests {
    private static Logger LOGGER = Logger.getLogger(CryptoServiceTest.class);

    private static final byte[] ENCRYPTED_TEST_DATA = new byte[]{};

    @Autowired
    @Qualifier("cryptoServiceClient")
    private ICryptoService cryptoService;

    @Test
    public void testEncryption() {
        byte[] encrypted = cryptoService.encrypt("SomeVeryImportantTestData");
        Assert.assertNotNull(encrypted);
        LOGGER.info("Information successfully encrypted " + Arrays.toString(encrypted));
    }

    @Test
    public void testDecryption() {
        String decrypted = cryptoService.decrypt(ENCRYPTED_TEST_DATA);
        Assert.assertNotNull(decrypted);
        LOGGER.info("Information successfully decrypted " + decrypted);
    }
}
