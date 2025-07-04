package it.jakegblp.nms.api.entity.metadata.keys;

import it.jakegblp.nms.api.entity.metadata.EntityDataSerializerInfo;
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
    private final Class<T> type;
    private final @NotNull EntityDataSerializerInfo.Type serializationType;

    public MetadataKey(Class<E> entityClass, int index, T defaultValue, @NotNull Class<T> type) {
        this(entityClass, index, defaultValue, type, EntityDataSerializerInfo.Type.NORMAL);
    }

    @SuppressWarnings("unchecked")
    public MetadataKey(Class<E> entityClass, int index, @NotNull T defaultValue) {
        this(entityClass, index, defaultValue, (Class<T>) defaultValue.getClass(), EntityDataSerializerInfo.Type.NORMAL);
    }
}
