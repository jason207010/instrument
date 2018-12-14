package com.instrument;

public class SimpleClassLoader extends ClassLoader {
    public Class<?> getClass(byte[] bytes) {
        return defineClass(null, bytes, 0, bytes.length);
    }
}