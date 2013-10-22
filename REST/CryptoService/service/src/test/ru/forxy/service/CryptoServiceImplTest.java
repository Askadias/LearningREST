package ru.forxy.service;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.forxy.crypto.ICryptoService;

import java.util.Arrays;

public class CryptoServiceImplTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(CryptoServiceImplTest.class);

    ICryptoService cryptoService = new CryptoServiceImpl("testPassword");

    @Test
    public void testEncrypt() {
        String text = "some text for encryption 1234567890 !@#$%^&*()";
        byte[] encrypted = cryptoService.encrypt(text);
        Assert.assertNotNull(encrypted);
        LOGGER.info("Information successfully encrypted {}", Arrays.toString(encrypted));
        String decrypted = cryptoService.decrypt(encrypted);
        LOGGER.info("Information successfully decrypted {}", decrypted);
        Assert.assertEquals(text, decrypted);
    }


    @Test
    public void testHash() {
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