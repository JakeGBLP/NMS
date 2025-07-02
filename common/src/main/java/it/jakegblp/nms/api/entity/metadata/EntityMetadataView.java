package it.jakegblp.nms.api.entity.metadata;

import it.jakegblp.nms.api.NMSAdapter;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Pose;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Optional;

public interface EntityMetadataView {

    enum Property implements BaseProperty {
        ENTITY_FLAGS(DefaultEntityData.raw(0, new EntityFlags(), EntityFlags.class, Byte.class)),
        AIR_TICKS(DefaultEntityData.simple(1, 300)),
        CUSTOM_NAME(DefaultEntityData.rawOptional(2, null, Component.class, NMSAdapter.nmsAdapter.getNMSComponentClass())),
        CUSTOM_NAME_VISIBILITY(DefaultEntityData.simple(3, false)),
        SILENT(DefaultEntityData.simple(4, false)),
        NO_GRAVITY(DefaultEntityData.simple(5, false)),
        POSE(DefaultEntityData.simple(6, Pose.STANDING)),
        TICKS_FROZEN(DefaultEntityData.simple(7, 0));

        private final DefaultEntityData<Entity, ?, ?> defaultEntityData;
        private final String name;

        Property(DefaultEntityData<Entity, ?, ?> defaultEntityData) {
            this.defaultEntityData = defaultEntityData;
            this.name = name().toLowerCase().replace('_', ' ');
        }

        @Override
        public int getIndex() {
            return defaultEntityData.index;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public EntityDataSerializerInfo<?> getSerializerInfo() {
            return new EntityDataSerializerInfo<>(defaultEntityData.valueClass, defaultEntityData.serializerInfoType);
        }

        public Object getValue() {
            return defaultEntityData.getValue();
        }
    }

    /**
     * Wrapper for the entity metadata bitmask at index 0.<br>
     * <a href="https://minecraft.wiki/w/Java_Edition_protocol/Entity_metadata#Entity">Minecraft Wiki – Entity Metadata</a>
     */
    class EntityFlags extends Flags<EntityFlags.EntityFlag> implements Cloneable {

        public enum EntityFlag implements Flag {
            ON_FIRE((byte) 0x01),
            SNEAKING((byte) 0x02),
            UNUSED((byte) 0x04), // Previously riding
            SPRINTING((byte) 0x08),
            SWIMMING((byte) 0x10),
            INVISIBLE((byte) 0x20),
            GLOWING((byte) 0x40),
            FLYING_ELYTRA((byte) 0x80);

            private final byte mask;

            EntityFlag(byte mask) {
                this.mask = mask;
            }

            @Override
            public byte getMask() {
                return mask;
            }
        }

        public EntityFlags() {
            flags = 0;
        }

        public EntityFlags(byte flags) {
            this.flags = flags;
        }

        public boolean isOnFire() {
            return getFlag(EntityFlag.ON_FIRE);
        }

        public void setOnFire(boolean value) {
            setFlag(EntityFlag.ON_FIRE, value);
        }

        public boolean isSneaking() {
            return getFlag(EntityFlag.SNEAKING);
        }

        public void setSneaking(boolean value) {
            setFlag(EntityFlag.SNEAKING, value);
        }

        public boolean isUnused() {
            return getFlag(EntityFlag.UNUSED);
        }

        public void setUnused(boolean value) {
            setFlag(EntityFlag.UNUSED, value);
        }

        public boolean isSprinting() {
            return getFlag(EntityFlag.SPRINTING);
        }

        public void setSprinting(boolean value) {
            setFlag(EntityFlag.SPRINTING, value);
        }

        public boolean isSwimming() {
            return getFlag(EntityFlag.SWIMMING);
        }

        public void setSwimming(boolean value) {
            setFlag(EntityFlag.SWIMMING, value);
        }

        public boolean isInvisible() {
            return getFlag(EntityFlag.INVISIBLE);
        }

        public void setInvisible(boolean value) {
            setFlag(EntityFlag.INVISIBLE, value);
        }

        public boolean isGlowing() {
            return getFlag(EntityFlag.GLOWING);
        }

        public void setGlowing(boolean value) {
            setFlag(EntityFlag.GLOWING, value);
        }

        public boolean isFlyingElytra() {
            return getFlag(EntityFlag.FLYING_ELYTRA);
        }

        public void setFlyingElytra(boolean value) {
            setFlag(EntityFlag.FLYING_ELYTRA, value);
        }

        @SuppressWarnings("CloneDoesntCallSuperClone")
        @Override
        public EntityFlags clone() {
            return new EntityFlags(this.flags);
        }

    }

    default @NotNull EntityFlags getEntityFlags() {
        return new EntityFlags();
    }

    default int getAirTicks() {
        return (Integer) Property.AIR_TICKS.defaultEntityData.getValue();
    }

    default @NotNull Optional<@Nullable Component> getCustomName() {
        return Optional.ofNullable((Component) Property.CUSTOM_NAME.getValue());
    }

    default boolean isCustomNameVisible() {
        return (Boolean) Property.CUSTOM_NAME_VISIBILITY.getValue();
    }

    default boolean isSilent() {
        return (Boolean) Property.SILENT.getValue();
    }

    default boolean hasNoGravity() {
        return (Boolean) Property.NO_GRAVITY.getValue();
    }

    default @NotNull Pose getPose() {
        return (Pose) Property.POSE.getValue();
    }

    default int getTicksFrozen() {
        return (Integer) Property.TICKS_FROZEN.getValue();
    }

    @SuppressWarnings("unchecked")
    default HashMap<BaseProperty, AbstractEntityData<? extends Entity, ?, ?>> getEntityDataItems() {
        HashMap<BaseProperty, AbstractEntityData<? extends Entity, ?, ?>> map = new HashMap<>();
        map.put(Property.ENTITY_FLAGS, DefaultEntityData.raw(0, getEntityFlags(), EntityFlags.class, Byte.class));
        map.put(Property.AIR_TICKS, DefaultEntityData.simple(1, getAirTicks()));
        map.put(Property.CUSTOM_NAME, DefaultEntityData.rawOptional(2, getCustomName().orElse(null), Component.class, NMSAdapter.nmsAdapter.getNMSComponentClass()));
        map.put(Property.CUSTOM_NAME_VISIBILITY, DefaultEntityData.simple(3, isCustomNameVisible()));
        map.put(Property.SILENT, DefaultEntityData.simple(4, isSilent()));
        map.put(Property.NO_GRAVITY, DefaultEntityData.simple(5, hasNoGravity()));
        map.put(Property.POSE, DefaultEntityData.raw(6, getPose(), Pose.class, NMSAdapter.nmsAdapter.getNMSPoseClass()));
        map.put(Property.TICKS_FROZEN, DefaultEntityData.simple(7, getTicksFrozen()));
        return map;
    }
}