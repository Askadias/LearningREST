package ru.forxy.rest;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.forxy.crypto.ICryptoService;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;

public class CryptoServiceImpl implements ICryptoService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CryptoServiceImpl.class);

    private static final String CIPHER_ALGORITHM = "AES/CBC/PKCS5Padding";
    private static final String DIGEST_ALGORITHM = "SHA-256";
    private static final String SECRET_KEY_SPEC = "AES";

    private final Object cryptoSync = new Object();
    private final Object hashSync = new Object();

    private MessageDigest digest;
    private Cipher cipher;
    private IvParameterSpec ivSpec;
    private SecretKeySpec secretKey;

    public CryptoServiceImpl(String password) {
        try {
            cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            digest = MessageDigest.getInstance(DIGEST_ALGORITHM);

            // setup an IV (initialization vector)
            byte[] iv = new byte[cipher.getBlockSize()];
            new SecureRandom().nextBytes(iv);

            ivSpec = new IvParameterSpec(iv);

            // hash keyString with SHA-256 and crop the output to 128-bit for key
            digest.update(password.getBytes());
            byte[] key = new byte[16];
            System.arraycopy(digest.digest(), 0, key, 0, key.length);
            secretKey = new SecretKeySpec(key, SECRET_KEY_SPEC);

        } catch (NoSuchAlgorithmException e) {
            LOGGER.error("Algorithm not found: " + CIPHER_ALGORITHM, e);
        } catch (NoSuchPaddingException e) {
            LOGGER.error("Invalid crypto algorithm was set in the CryptoService implementation: " + CIPHER_ALGORITHM, e);
        }
    }

    @Override
    public byte[] encrypt(String decrypted) {
        synchronized (cryptoSync) {
            try {
                cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);
                return cipher.doFinal(decrypted.getBytes());
            } catch (IllegalBlockSizeException e) {
                LOGGER.error("Invalid data size", e);
            } catch (BadPaddingException e) {
                LOGGER.error("Invalid cipher state", e);
            } catch (InvalidKeyException e) {
                LOGGER.error("Invalid key", e);
            } catch (InvalidAlgorithmParameterException e) {
                LOGGER.error("Invalid parameter", e);
            }
        }
        return null;
    }

    @Override
    public String decrypt(byte[] encrypted) {
        synchronized (cryptoSync) {
            try {
                cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec);
                return new String(cipher.doFinal(encrypted));
            } catch (IllegalBlockSizeException e) {
                LOGGER.error("Invalid data size", e);
            } catch (BadPaddingException e) {
                LOGGER.error("Invalid cipher state", e);
            } catch (InvalidKeyException e) {
                LOGGER.error("Invalid key", e);
            } catch (InvalidAlgorithmParameterException e) {
                LOGGER.error("Invalid parameter", e);
            }
        }
        return null;
    }

    @Override
    public byte[] hash(String value) {
        synchronized (hashSync) {
            return digest.digest(value.getBytes());
        }
    }
}
