package ru.forxy.service;


import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;

public class CryptoServiceImpl implements ICryptoService {

    private static final String CIPHER_ALGORITHM = "AES/CBC/PKCS5Padding";
    private static final String DIGEST_ALGORITHM = "SHA-256";
    private static final String SECRET_KEY_SPEC = "AES"; 
    private MessageDigest digest;
    private final Object cryptoSync = new Object();
    private final Object hashSync = new Object();

    private IvParameterSpec ivSpec;
    private Cipher cipher;
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
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    @Override
    public byte[] encrypt(String decrypted) {
        synchronized (cryptoSync) {
            try {
                cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);
                return cipher.doFinal(decrypted.getBytes());
            } catch (IllegalBlockSizeException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } catch (BadPaddingException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } catch (InvalidKeyException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } catch (InvalidAlgorithmParameterException e) {
                e.printStackTrace();
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
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } catch (BadPaddingException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } catch (InvalidKeyException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } catch (InvalidAlgorithmParameterException e) {
                e.printStackTrace();
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
