package it.jakegblp.nms.api.utils;

import org.bukkit.Bukkit;

import java.lang.reflect.*;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public interface ReflectionUtils {

    record MethodKey(Class<?> clazz, String name, List<Class<?>> parameterTypes) {
        public MethodKey(Class<?> clazz, String name, Class<?>... parameterTypes) {
            this(clazz, name, Arrays.asList(parameterTypes.clone()));
        }
    }

    record FieldKey(Class<?> clazz, String name) {}

    Map<String, Class<?>> CACHED_CLASSES = new ConcurrentHashMap<>();
    Map<Class<?>, Constructor<?>> CACHED_CONSTRUCTORS = new ConcurrentHashMap<>();
    Map<MethodKey, Method> CACHED_METHODS = new ConcurrentHashMap<>();
    Map<MethodKey, Method> CACHED_DECLARED_METHODS = new ConcurrentHashMap<>();
    Map<FieldKey, Field> CACHED_FIELDS = new ConcurrentHashMap<>();
    Map<FieldKey, Field> CACHED_DECLARED_FIELDS = new ConcurrentHashMap<>();

    static Constructor<?> getDeclaredConstructor(Class<?> clazz, Class<?>... parameterTypes) {
        return CACHED_CONSTRUCTORS.computeIfAbsent(clazz, key -> {
            try {
                return clazz.getDeclaredConstructor(parameterTypes);
            } catch (NoSuchMethodException e) {
                Bukkit.getLogger().severe("REFLECTION ERROR: " + e.getMessage());
                return null;
            }
        });
    }
    static Constructor<?> getPrivateDeclaredConstructor(Class<?> clazz, Class<?>... parameterTypes) {
        Constructor<?> constructor = getDeclaredConstructor(clazz, parameterTypes);
        if (constructor == null) return null;
        constructor.setAccessible(true);
        return constructor;
    }


    static Class<?> getClass(String name) {
        return CACHED_CLASSES.computeIfAbsent(name, key -> {
            try {
                return Class.forName(key);
            } catch (ClassNotFoundException e) {
                Bukkit.getLogger().severe("REFLECTION ERROR: " + e.getMessage());
                return null;
            }
        });
    }

    static Method getMethod(Class<?> clazz, String name, Class<?>... parameterTypes) {
        return CACHED_METHODS.computeIfAbsent(new MethodKey(clazz, name, parameterTypes), key -> {
            try {
                return clazz.getMethod(name, parameterTypes);
            } catch (NoSuchMethodException e) {
                Bukkit.getLogger().severe("REFLECTION ERROR: " + e.getMessage());
                return null;
            }
        });
    }

    static Method getDeclaredMethod(Class<?> clazz, String name, Class<?>... parameterTypes) {
        return CACHED_DECLARED_METHODS.computeIfAbsent(new MethodKey(clazz, name, parameterTypes), key -> {
            try {
                return clazz.getDeclaredMethod(name, parameterTypes);
            } catch (NoSuchMethodException e) {
                Bukkit.getLogger().severe("REFLECTION ERROR: " + e.getMessage());
                return null;
            }
        });
    }

    static Field getField(Class<?> clazz, String name) {
        return CACHED_FIELDS.computeIfAbsent(new FieldKey(clazz, name), key -> {
            try {
                return clazz.getField(name);
            } catch (NoSuchFieldException e) {
                Bukkit.getLogger().severe("REFLECTION ERROR: " + e.getMessage());
                return null;
            }
        });
    }

    static Field getDeclaredField(Class<?> clazz, String name) {
        return CACHED_DECLARED_FIELDS.computeIfAbsent(new FieldKey(clazz, name), key -> {
            try {
                return clazz.getDeclaredField(name);
            } catch (NoSuchFieldException e) {
                Bukkit.getLogger().severe("REFLECTION ERROR: " + e.getMessage());
                return null;
            }
        });
    }


    static Object getFieldValueSafely(Field field, Object object) {
        try {
            return field.get(object);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    static Object getPrivateFieldValueSafely(Field field, Object object) {
        try {
            field.setAccessible(true);
            return field.get(object);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    static void setPrivateFieldValueSafely(Field field, Object object, Object value) {
        try {
            field.setAccessible(true);
            field.set(object, value);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
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

    static Object newInstance(Constructor<?> constructor, Object... args) {
        try {
            return constructor.newInstance(args);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            return null;
        }
    }

    static Class<?> getFirstGenericType(Object object) {
        Type superclass = object.getClass().getGenericSuperclass();
        if (superclass instanceof ParameterizedType parameterizedType) {
            Type typeArgument = parameterizedType.getActualTypeArguments()[0];
            if (typeArgument instanceof Class<?>)
                return (Class<?>) typeArgument;
        }
        return null;
    }
}
