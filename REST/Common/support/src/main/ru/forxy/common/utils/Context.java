package ru.forxy.common.utils;

import java.util.HashMap;

public class Context {

    private static ThreadLocal<HashMap<String, Object>> context = new ThreadLocal<HashMap<String, Object>>();

    public static void put(String key, Object object) {
        HashMap<String, Object> storage = context.get();
        storage.put(key, object);
    }

    public static Object get(String key) {
        return context.get().get(key);
    }
}
