package it.jakegblp.nms.api.entity.metadata;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NullMarked;

import java.util.Optional;

import static it.jakegblp.nms.api.entity.metadata.EntityDataSerializerInfo.Type.NORMAL;
import static it.jakegblp.nms.api.entity.metadata.EntityDataSerializerInfo.Type.OPTIONAL;
import static it.jakegblp.nms.impl.SharedUtils.asNMS;

@Getter
@Setter
public class EntityData<E extends Entity, T, R>
        extends AbstractEntityData<E, T, R>
        implements AbstractEntityData.View<T>, AbstractEntityData.Controller<T> {

    protected @Nullable T value;

    public static <E extends Entity, T> EntityData<E, T, T> simple(int index, T value) {
        return new EntityData<>(index, value);
    }
    public static <E extends Entity, T> EntityData<E, T, T> simpleDefault(int index, T value, T defaultValue) {
        return new EntityData<>(index, value, defaultValue);
    }
    public static <E extends Entity, T> EntityData<E, T, T> simpleOptional(int index, T value) {
        return new EntityData<>(index, value, OPTIONAL);
    }
    public static <E extends Entity, T> EntityData<E, T, T> simpleEmptyOptional(int index, T value, Class<T> valueClass) {
        return new EntityData<>(index, value, valueClass, OPTIONAL);
    }
    public static <E extends Entity, T, R> EntityData<E, T, R> rawOptional(int index, T value, Class<T> valueClass, Class<R> rawValueClass) {
        return new EntityData<>(index, value, valueClass, rawValueClass, OPTIONAL);
    }
    public static <E extends Entity, T, R> EntityData<E, T, R> rawDefault(int index, T value, T defaultValue, Class<T> valueClass, Class<R> rawValueClass) {
        return new EntityData<>(index, value, defaultValue, valueClass, rawValueClass);
    }
    public static <E extends Entity, T, R> EntityData<E, T, R> rawDefaultOptional(int index, T value, T defaultValue, Class<T> valueClass, Class<R> rawValueClass) {
        return new EntityData<>(index, value, defaultValue, valueClass, rawValueClass, OPTIONAL);
    }

    @NullMarked
    public EntityData(int index, Class<E> entityType, @Nullable T value, @Nullable T defaultValue, Class<T> valueClass, Class<R> rawValueClass, EntityDataSerializerInfo.Type serializerInfoType) {
        super(index, entityType, defaultValue, valueClass, rawValueClass, serializerInfoType);
        this.value = value;
    }

    @NullMarked
    @SuppressWarnings("unchecked")
    public EntityData(int index, Class<E> entityType, @Nullable T value, @Nullable T defaultValue, Class<T> valueClass, EntityDataSerializerInfo.Type serializerInfoType) {
        this(index, entityType, value, defaultValue, valueClass, (Class<R>) valueClass, serializerInfoType);
    }

    @NullMarked
    public EntityData(int index, Class<E> entityType, @Nullable T value, @Nullable T defaultValue, Class<T> valueClass, Class<R> rawValueClass) {
        this(index, entityType, value, defaultValue, valueClass, rawValueClass, NORMAL);
    }

    @NullMarked
    @SuppressWarnings("unchecked")
    public EntityData(int index, Class<E> entityType, @Nullable T value, @Nullable T defaultValue, Class<T> valueClass) {
        this(index, entityType, value, defaultValue, valueClass, (Class<R>) valueClass, NORMAL);
    }

    @NullMarked
    @SuppressWarnings("unchecked")
    public EntityData(int index, Class<E> entityType, T value, T defaultValue) {
        this(index, entityType, value, defaultValue, (Class<T>) defaultValue.getClass());
    }

    @NullMarked
    @SuppressWarnings("unchecked")
    public EntityData(int index, Class<E> entityType, T value, T defaultValue, EntityDataSerializerInfo.Type serializerInfoType) {
        this(index, entityType, value, defaultValue, (Class<T>) defaultValue.getClass(), serializerInfoType);
    }

    @NullMarked
    @SuppressWarnings("unchecked")
    public EntityData(int index, @Nullable T value, @Nullable T defaultValue, Class<T> valueClass, Class<R> rawValueClass) {
        this(index, (Class<E>) Entity.class, value, defaultValue, valueClass, rawValueClass);
    }

    @NullMarked
    @SuppressWarnings("unchecked")
    public EntityData(int index, @Nullable T value, @Nullable T defaultValue, Class<T> valueClass) {
        this(index, (Class<E>) Entity.class, value, defaultValue, valueClass);
    }

    @NullMarked
    @SuppressWarnings("unchecked")
    public EntityData(int index, @Nullable T value, @Nullable T defaultValue, Class<T> valueClass, Class<R> rawValueClass, EntityDataSerializerInfo.Type serializerInfoType) {
        this(index, (Class<E>) Entity.class, value, defaultValue, valueClass, rawValueClass, serializerInfoType);
    }

    @NullMarked
    @SuppressWarnings("unchecked")
    public EntityData(int index, @Nullable T value, @Nullable T defaultValue, Class<T> valueClass, EntityDataSerializerInfo.Type serializerInfoType) {
        this(index, (Class<E>) Entity.class, value, defaultValue, valueClass, serializerInfoType);
    }

    @NullMarked
    public EntityData(int index, @Nullable T value, Class<T> valueClass, EntityDataSerializerInfo.Type serializerInfoType) {
        this(index, value, value, valueClass, serializerInfoType);
    }

    @NullMarked
    public EntityData(int index, @Nullable T value, Class<T> valueClass, Class<R> rawValueClass, EntityDataSerializerInfo.Type serializerInfoType) {
        this(index, value, value, valueClass, rawValueClass, serializerInfoType);
    }

    @NullMarked
    @SuppressWarnings("unchecked")
    public EntityData(int index, T value, T defaultValue) {
        this(index, (Class<E>) Entity.class, value, defaultValue);
    }

    @NullMarked
    @SuppressWarnings("unchecked")
    public EntityData(int index, T value, T defaultValue, EntityDataSerializerInfo.Type serializerInfoType) {
        this(index, (Class<E>) Entity.class, value, defaultValue, serializerInfoType);
    }

    @NullMarked
    public EntityData(int index, T value) {
        this(index, value, value);
    }

    @NullMarked
    public EntityData(int index, T value, EntityDataSerializerInfo.Type serializerInfoType) {
        this(index, value, value, serializerInfoType);
    }

    @Override
    @SuppressWarnings("unchecked")
    public @Nullable R getRawValue() {
        return (R) asNMS(value);
    }

    @Override
    public Optional<T> getOptionalValue() {
        return Optional.ofNullable(value);
    }

    @Override
    public EntityData<?, T, ?> setValue(T value) {
        this.value = value;
        return this;
    }
}
