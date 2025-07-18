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
    private final T defaultValue;
    private final Class<T> objectClass;
    private final @NotNull EntitySerializerInfo entitySerializerInfo;

    public MetadataKey(Class<E> entityClass, int index, T defaultValue, @NotNull Class<T> type) {
        this(entityClass, index, defaultValue, type, EntitySerializerInfo.Type.NORMAL);
    }

    public MetadataKey(Class<E> entityClass, int index, T defaultValue, @NotNull Class<T> type, EntitySerializerInfo.Type serializerType) {
        this(entityClass, index, defaultValue, type, new EntitySerializerInfo(type, serializerType));
    }

    @SuppressWarnings("unchecked")
    public MetadataKey(Class<E> entityClass, int index, @NotNull T defaultValue) {
        this(entityClass, index, defaultValue, (Class<T>) defaultValue.getClass(), new EntitySerializerInfo(defaultValue.getClass(), EntitySerializerInfo.Type.NORMAL));
    }
}
