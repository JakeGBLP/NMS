package it.jakegblp.nms.api.entity.metadata;

import lombok.Getter;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NullMarked;

import java.util.Optional;

import static it.jakegblp.nms.impl.SharedUtils.asNMS;

@Getter
public abstract class AbstractEntityData<E extends Entity, T, R> {

    public interface View<T> {

        /**
         * @apiNote this method returns the value <b>only</b> for instances of entity data that have one.
         * {@link DefaultEntityData Default Entity Data} implements this and returns the default value.
         * @return the main value associated with this entity data.
         */
        T getValue();
    }

    public interface Controller<T> {

        /**
         * @apiNote this method set the value <b>only</b> for instances of entity data that have one.
         * {@link DefaultEntityData Default Entity Data} implements this and sets the default value.
         */
        EntityData<?, T, ?> setValue(T value);
    }

    protected final int index;
    protected final Class<E> entityClass;
    protected final @Nullable T defaultValue;
    protected final @NotNull Class<T> valueClass;
    protected final @NotNull Class<R> rawValueClass;
    protected final @NotNull EntityDataSerializerInfo.Type serializerInfoType;

    protected AbstractEntityData(
            int index,
            @NotNull Class<E> entityClass,
            @Nullable T defaultValue,
            @NotNull Class<T> valueClass,
            @NotNull Class<R> rawValueClass,
            @NotNull EntityDataSerializerInfo.Type serializerInfoType
    ) {
        this.index = index;
        this.entityClass = entityClass;
        this.defaultValue = defaultValue;
        this.valueClass = valueClass;
        this.rawValueClass = rawValueClass;
        this.serializerInfoType = serializerInfoType;
    }

    public abstract @Nullable R getRawValue();

    @NullMarked
    public abstract Optional<T> getOptionalValue();

    @SuppressWarnings("unchecked")
    public @Nullable R getRawDefaultValue() {
        return (R) asNMS(defaultValue);
    }

    @NullMarked
    public Optional<R> getOptionalRawValue() {
        return Optional.ofNullable(getRawValue());
    }
}