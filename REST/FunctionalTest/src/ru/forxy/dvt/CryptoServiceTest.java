package ru.forxy.dvt;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import ru.forxy.BaseSpringContextTest;
import ru.forxy.crypto.ICryptoService;

import java.util.Arrays;

public class CryptoServiceTest extends BaseSpringContextTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(CryptoServiceTest.class);

    @Autowired
    @Qualifier("cryptoServiceClient")
    private ICryptoService cryptoService;

    @Test
    public void testEncryption() {
        String text = "some text for encryption 1234567890 !@#$%^&*()";
        byte[] encrypted = cryptoService.encrypt(text);
        Assert.assertNotNull(encrypted);
        LOGGER.info("Information successfully encrypted: {}", Arrays.toString(encrypted));
        String decrypted = cryptoService.decrypt(encrypted);
        LOGGER.info("Information successfully decrypted: {}", decrypted);
        Assert.assertEquals(text, decrypted);
    }

    @Test
    public void testHashing() {
        byte[] passwordHash = cryptoService.hash("password");
        Assert.assertNotNull(passwordHash);
        LOGGER.info("'Password' successfully encrypted: {}", Arrays.toString(passwordHash));
        byte[] passwordHash2 = cryptoService.hash("password");
        Assert.assertTrue(Arrays.equals(passwordHash, passwordHash2));
        LOGGER.info("'Password' successfully encrypted: {}", Arrays.toString(passwordHash2));
        byte[] passwordHash3 = cryptoService.hash("different");
        Assert.assertFalse(Arrays.equals(passwordHash, passwordHash3));
        LOGGER.info("'Different' successfully encrypted: {}", Arrays.toString(passwordHash3));
    }
}
