package it.jakegblp.nms.api.entity.metadata;

import org.jetbrains.annotations.NotNull;

public record EntitySerializerInfo<T>(@NotNull Class<T> serializerClass, @NotNull Type serializerType) {

    public EntitySerializerInfo(@NotNull Class<T> serializerClass) {
        this(serializerClass, Type.NORMAL);
    }

    public static <T> EntitySerializerInfo<T> normal(@NotNull Class<T> serializationClass) {
        return new EntitySerializerInfo<>(serializationClass, Type.NORMAL);
    }
    public static <T> EntitySerializerInfo<T> optional(@NotNull Class<T> serializationClass) {
        return new EntitySerializerInfo<>(serializationClass, Type.OPTIONAL);
    }
    public static <T> EntitySerializerInfo<T> holder(@NotNull Class<T> serializationClass) {
        return new EntitySerializerInfo<>(serializationClass, Type.HOLDER);
    }
    public static <T> EntitySerializerInfo<T> list(@NotNull Class<T> serializationClass) {
        return new EntitySerializerInfo<>(serializationClass, Type.LIST);
    }

    public enum Type {
        OPTIONAL, HOLDER, LIST, NORMAL
    }

}