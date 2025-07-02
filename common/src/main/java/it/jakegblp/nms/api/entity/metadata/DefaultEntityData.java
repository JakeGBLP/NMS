package it.jakegblp.nms.api.entity.metadata;

import com.google.errorprone.annotations.Immutable;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

import static it.jakegblp.nms.api.entity.metadata.EntityDataSerializerInfo.Type.NORMAL;
import static it.jakegblp.nms.api.entity.metadata.EntityDataSerializerInfo.Type.OPTIONAL;

@Immutable
public class DefaultEntityData<E extends Entity, T, R>
        extends AbstractEntityData<E, T, R>
        implements AbstractEntityData.View<T>, AbstractEntityData.Controller<T> {

    public DefaultEntityData(
            int index,
            Class<E> entityClass,
            @Nullable T defaultValue,
            @NotNull Class<T> valueClass,
            @NotNull Class<R> rawValueClass,
            @NotNull EntityDataSerializerInfo.Type serializerInfoType) {
        super(index,entityClass, defaultValue, valueClass, rawValueClass, serializerInfoType);
    }

    @Override
    public @Nullable R getRawValue() {
        return getRawDefaultValue();
    }

    @Override
    public Optional<T> getOptionalValue() {
        return Optional.ofNullable(getDefaultValue());
    }

    @SuppressWarnings("unchecked")
    public static <E extends Entity, T> DefaultEntityData<E, T, T> simple(int index, T defaultValue) {
        Class<T> defaultValueClass = (Class<T>) defaultValue.getClass();
        return new DefaultEntityData<>(index, (Class<E>) Entity.class, defaultValue, defaultValueClass, defaultValueClass, NORMAL);
    }
    @SuppressWarnings("unchecked")
    public static <E extends Entity, T> DefaultEntityData<E, T, T> simpleOptional(int index, T defaultValue) {
        Class<T> defaultValueClass = (Class<T>) defaultValue.getClass();
        return new DefaultEntityData<>(index, (Class<E>) Entity.class, defaultValue, defaultValueClass, defaultValueClass, OPTIONAL);
    }
    @SuppressWarnings("unchecked")
    public static <E extends Entity, T> DefaultEntityData<E, T, T> simpleEmptyOptional(int index, Class<T> valueClass) {
        return new DefaultEntityData<>(index, (Class<E>) Entity.class, null, valueClass, valueClass, OPTIONAL);
    }
    @SuppressWarnings("unchecked")
    public static <E extends Entity, T, R> DefaultEntityData<E, T, R> raw(int index, T defaultValue, Class<T> valueClass, Class<R> rawValueClass) {
        return new DefaultEntityData<>(index, (Class<E>) Entity.class, defaultValue, valueClass, rawValueClass, NORMAL);
    }
    @SuppressWarnings("unchecked")
    public static <E extends Entity, T, R> DefaultEntityData<E, T, R> rawOptional(int index, T defaultValue, Class<T> valueClass, Class<R> rawValueClass) {
        return new DefaultEntityData<>(index, (Class<E>) Entity.class, defaultValue, valueClass, rawValueClass, OPTIONAL);
    }

    @Override
    public T getValue() {
        return defaultValue;
    }

    @Override
    public EntityData<?, T, ?> setValue(T value) {
        return new EntityData<>(index, entityClass, value, defaultValue, valueClass, rawValueClass, serializerInfoType);
    }
}
