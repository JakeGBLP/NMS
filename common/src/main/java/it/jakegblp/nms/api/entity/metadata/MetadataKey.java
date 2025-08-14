package it.jakegblp.nms.api.entity.metadata;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

@AllArgsConstructor
@Getter
public class MetadataKey<E extends Entity, T> {
    private final Class<E> entityClass;
    private final int index;
    private final T value;
    private final Class<T> objectClass;
    private final @NotNull EntitySerializerInfo entitySerializerInfo;

    public MetadataKey(Class<E> entityClass, int index, T value, @NotNull Class<T> type) {
        this(entityClass, index, value, type, EntitySerializerInfo.Type.NORMAL);
    }

    public MetadataKey(Class<E> entityClass, int index, T value, @NotNull Class<T> type, EntitySerializerInfo.Type serializerType) {
        this(entityClass, index, value, type, new EntitySerializerInfo(type, serializerType));
    }

    @SuppressWarnings("unchecked")
    public MetadataKey(Class<E> entityClass, int index, @NotNull T value) {
        this(entityClass, index, value, (Class<T>) value.getClass(), new EntitySerializerInfo(value.getClass(), EntitySerializerInfo.Type.NORMAL));
    }

    public static <E extends Entity, T> Named<E, T> named(String name, MetadataKey<E, T> metadataKey) {
        return new Named<>(name, metadataKey.getEntityClass(), metadataKey.getIndex(), metadataKey.getValue(), metadataKey.getObjectClass(), metadataKey.getEntitySerializerInfo());
    }

    @Getter
    public static class Named<E extends Entity, T> extends MetadataKey<E, T> {

        private final String name;

        public Named(String name, Class<E> entityClass, int index, T value, Class<T> objectClass, @NotNull EntitySerializerInfo entitySerializerInfo) {
            super(entityClass, index, value, objectClass, entitySerializerInfo);
            this.name = name;
        }

        public Named(String name, Class<E> entityClass, int index, T value, @NotNull Class<T> type) {
            super(entityClass, index, value, type);
            this.name = name;
        }

        public Named(String name, Class<E> entityClass, int index, T value, @NotNull Class<T> type, EntitySerializerInfo.Type serializerType) {
            super(entityClass, index, value, type, serializerType);
            this.name = name;
        }

        public Named(String name, Class<E> entityClass, int index, @NotNull T value) {
            super(entityClass, index, value);
            this.name = name;
        }
    }
}