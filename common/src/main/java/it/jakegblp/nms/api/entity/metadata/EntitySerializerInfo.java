package it.jakegblp.nms.api.entity.metadata;

import org.jetbrains.annotations.NotNull;

public record EntitySerializerInfo(@NotNull Class<?> serializerClass, @NotNull Type serializerType) {

    public EntitySerializerInfo(@NotNull Class<?> serializerClass) {
        this(serializerClass, Type.NORMAL);
    }

    public static EntitySerializerInfo normal(@NotNull Class<?> serializationClass) {
        return new EntitySerializerInfo(serializationClass, Type.NORMAL);
    }

    public static EntitySerializerInfo optional(@NotNull Class<?> serializationClass) {
        return new EntitySerializerInfo(serializationClass, Type.OPTIONAL);
    }

    public static EntitySerializerInfo holder(@NotNull Class<?> serializationClass) {
        return new EntitySerializerInfo(serializationClass, Type.HOLDER);
    }

    public static EntitySerializerInfo list(@NotNull Class<?> serializationClass) {
        return new EntitySerializerInfo(serializationClass, Type.LIST);
    }

    public enum Type {
        OPTIONAL, HOLDER, LIST, NORMAL
    }

}