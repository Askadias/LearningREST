package ru.forxy.common;

import java.util.HashMap;

public class ExecutionContext {

    private static ThreadLocal<HashMap<String, Object>> context;

    public static void put(String key, Object object) {
        HashMap<String, Object> storage = context.get();
        storage.put(key, object);
    }

    public static Object get(String key) {
        return context.get().get(key);
    }
}
