package ru.forxy.service;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Date;

public class CryptoServiceImplTest {

    private static final Logger LOGGER = LogManager.getLogger(CryptoServiceImplTest.class);

    ICryptoService cryptoService = new CryptoServiceImpl("AES/ECB/PKCS5Padding", "PBKDF2WithHmacSHA1", "test salt", 1000, "testPassword");

    @Test
    public void testEncrypt() {
        String text = "some text for encription 1234567890 !@#$%^&*()";
        byte[] encrypted = cryptoService.encrypt("string");
        Assert.assertNotNull(encrypted);
        String decrypted = cryptoService.decrypt(encrypted);
        Assert.assertEquals(text, decrypted);
    }


    @Test
    public void testHash() {
        long timeStart = new Date().getTime();
        byte[] passwordHash = cryptoService.hash("password");
        LOGGER.info("Hash duration: " + (new Date().getTime() - timeStart) + "ms");
        Assert.assertNotNull(passwordHash);
        byte[] passwordHash2 = cryptoService.hash("password");
        Assert.assertTrue(Arrays.equals(passwordHash, passwordHash2));
        byte[] passwordHash3 = cryptoService.hash("different");
        Assert.assertFalse(Arrays.equals(passwordHash, passwordHash3));
    }
}
