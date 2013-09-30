package ru.forxy.service;

import org.junit.Assert;
import org.junit.Test;

public class CryptoServiceImplTest {

    ICryptoService cryptoService = new CryptoServiceImpl();

    @Test
    public void testEncrypt() {
        byte[] encrypted = cryptoService.encrypt("string");
        Assert.assertNotNull(encrypted);
    }

    @Test
    public void testDecrypt() {
        String decrypted = cryptoService.decrypt(new byte[]{65, 42, 65, 98});
        Assert.assertNotNull(decrypted);
    }
}
