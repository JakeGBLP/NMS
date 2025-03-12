package it.jakegblp.nms.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public interface ReflectionUtils {

    record MethodKey(Class<?> clazz, String name, List<Class<?>> parameterTypes) {
        public MethodKey(Class<?> clazz, String name, Class<?>... parameterTypes) {
            this(clazz, name, Arrays.asList(parameterTypes.clone()));
        }
    }

    record FieldKey(Class<?> clazz, String name) {}

    Map<String, Class<?>> CACHED_CLASSES = new ConcurrentHashMap<>();
    Map<MethodKey, Method> CACHED_METHODS = new ConcurrentHashMap<>();
    Map<MethodKey, Method> CACHED_DECLARED_METHODS = new ConcurrentHashMap<>();
    Map<FieldKey, Field> CACHED_FIELDS = new ConcurrentHashMap<>();

    static Class<?> getClassSafely(String name) {
        try {
            return Class.forName(name);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    static Class<?> getCachedClass(String name) {
        return CACHED_CLASSES.computeIfAbsent(name, ReflectionUtils::getClassSafely);
    }

    static Method getMethodSafely(Class<?> clazz, String name, Class<?>... parameterTypes) {
        try {
            return clazz.getMethod(name, parameterTypes);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            return null;
        }
    }

    static Method getCachedMethod(Class<?> clazz, String name, Class<?>... parameterTypes) {
        return CACHED_METHODS.computeIfAbsent(new MethodKey(clazz, name, parameterTypes),
                key -> getMethodSafely(clazz, name, parameterTypes));
    }

    static Method getDeclaredMethodSafely(Class<?> clazz, String name, Class<?>... parameterTypes) {
        try {
            Method method = clazz.getDeclaredMethod(name, parameterTypes);
            return method;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            return null;
        }
    }

    static Method getCachedDeclaredMethod(Class<?> clazz, String name, Class<?>... parameterTypes) {
        return CACHED_DECLARED_METHODS.computeIfAbsent(new MethodKey(clazz, name, parameterTypes),
                key -> getDeclaredMethodSafely(clazz, name, parameterTypes));
    }

    static Field getFieldSafely(Class<?> clazz, String name) {
        try {
            return clazz.getField(name);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            return null;
        }
    }

    static Field getCachedField(Class<?> clazz, String name) {
        return CACHED_FIELDS.computeIfAbsent(new FieldKey(clazz, name),
                key -> getFieldSafely(clazz, name));
    }

    static Object getFieldValueSafely(Field field, Object object) {
        try {
            return field.get(object);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    static Object invokeSafely(Method method, Object object, Object... args) {
        try {
            return method.invoke(object, args);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            return null;
        }
    }
}
