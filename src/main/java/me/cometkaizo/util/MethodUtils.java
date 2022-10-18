package me.cometkaizo.util;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;

@SuppressWarnings("unused")
public class MethodUtils {

    public static @NotNull Method forNameStrict(Class<?> clazz, String name, Class<?>... parameterTypes) {
        try {
            return clazz.getMethod(name, parameterTypes);
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
