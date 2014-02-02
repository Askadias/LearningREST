package ru.forxy.fraud.rest.pojo.rs;

public class BlackList {
    private Type type;
    private String value;

    public enum Type {
        Email,
        CC,
        GUID
    }
}
