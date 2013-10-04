package ru.forxy.service;


import javax.crypto.*;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class CryptoServiceImpl implements ICryptoService {

    private MessageDigest sha256;
    private final Object cryptoSync = new Object();
    private final Object hashSync = new Object();

    private Cipher aes;
    private SecretKeySpec secretKey;

    public CryptoServiceImpl(String cryptoAlgorithm, String keyAlgorithm, String salt, int iterations, String password) {
        try {
            aes = Cipher.getInstance(cryptoAlgorithm);
            SecretKeyFactory factory = SecretKeyFactory.getInstance(keyAlgorithm);
            SecretKey tmp = factory.generateSecret(new PBEKeySpec(password.toCharArray(), salt.getBytes(), iterations, 128));
            secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");
            sha256 = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    @Override
    public byte[] encrypt(String decrypted) {
        synchronized (cryptoSync) {
            try {
                aes.init(Cipher.ENCRYPT_MODE, secretKey);
                return aes.doFinal(decrypted.getBytes());
            } catch (IllegalBlockSizeException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } catch (BadPaddingException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } catch (InvalidKeyException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
        return null;
    }

    @Override
    public String decrypt(byte[] encrypted) {
        synchronized (cryptoSync) {
            try {
                aes.init(Cipher.DECRYPT_MODE, secretKey);
                return new String(aes.doFinal(encrypted));
            } catch (IllegalBlockSizeException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } catch (BadPaddingException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } catch (InvalidKeyException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
        return null;
    }

    @Override
    public byte[] hash(String value) {
        synchronized (hashSync) {
            return sha256.digest(value.getBytes());
        }
    }
}
