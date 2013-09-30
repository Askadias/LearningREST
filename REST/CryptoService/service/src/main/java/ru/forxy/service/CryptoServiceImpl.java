package ru.forxy.service;


import ru.forxy.util.Security;

public class CryptoServiceImpl implements ICryptoService {

    @Override
    public byte[] encrypt(String value) {
        return Security.md5(value);
    }

    @Override
    public String decrypt(byte[] value) {
        return "bytes";
    }
}
