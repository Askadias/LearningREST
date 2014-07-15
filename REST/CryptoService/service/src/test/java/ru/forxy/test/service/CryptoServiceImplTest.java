package ru.forxy.test.service;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.forxy.crypto.ICryptoService;
import ru.forxy.crypto.rest.CryptoServiceImpl;

import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.Phaser;

public class CryptoServiceImplTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(CryptoServiceImplTest.class);

    ICryptoService cryptoService = new CryptoServiceImpl("testPassword");

    private static final int THREADS_COUNT = 4098;

    @Test
    public void testEncrypt() {
        final String text = "some text for encryption 1234567890 !@#$%^&*()";
        final byte[] encrypted = cryptoService.encrypt(text);
        Assert.assertNotNull(encrypted);
        LOGGER.trace("Information successfully encrypted {}", Arrays.toString(encrypted));
        String decrypted = cryptoService.decrypt(encrypted);
        LOGGER.trace("Information successfully decrypted {}", decrypted);
        Assert.assertEquals(text, decrypted);
    }


    @Test
    public void testHash() {
        final byte[] passwordHash = cryptoService.hash("password");
        Assert.assertNotNull(passwordHash);
        LOGGER.trace("'Password' successfully encrypted: {}", Arrays.toString(passwordHash));
        final byte[] passwordHash2 = cryptoService.hash("password");
        Assert.assertTrue(Arrays.equals(passwordHash, passwordHash2));
        LOGGER.trace("'Password' successfully encrypted: {}", Arrays.toString(passwordHash2));
        final byte[] passwordHash3 = cryptoService.hash("different");
        Assert.assertFalse(Arrays.equals(passwordHash, passwordHash3));
        LOGGER.trace("'Different' successfully encrypted: {}", Arrays.toString(passwordHash3));
    }

    @Test
    public void testPerformance() {
        final long start = new Date().getTime();
        final Phaser phaser = new Phaser(THREADS_COUNT);
        final Executor executor = Executors.newFixedThreadPool(THREADS_COUNT);
        for (int i = 0; i < THREADS_COUNT; i++) {
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    testEncrypt();
                    phaser.arrive();
                }
            });
        }
        phaser.awaitAdvance(0);
        LOGGER.info("Execution time = {}ms", new Date().getTime() - start);
    }
}
