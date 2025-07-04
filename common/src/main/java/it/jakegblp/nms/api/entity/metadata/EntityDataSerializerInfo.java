package it.jakegblp.nms.api.entity.metadata;

import org.jetbrains.annotations.NotNull;

public record EntityDataSerializerInfo<T>(@NotNull Class<T> tClass, @NotNull EntityDataSerializerInfo.Type type) {

    public EntityDataSerializerInfo(@NotNull Class<T> clazz) {
        this(clazz, Type.NORMAL);
    }

    public enum Type {
        OPTIONAL,
        LIST,
        HOLDER,
        NORMAL;
    }

}