package it.jakegblp.nms.api.entity.metadata;

import it.jakegblp.nms.api.NMSAdapter;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.util.BlockVector;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Optional;

import static it.jakegblp.nms.api.entity.metadata.LivingEntityMetadataView.Property.*;

public interface LivingEntityMetadataView extends EntityMetadataView {

    enum Property implements BaseProperty {
        HAND_STATES(8, HandStates.class),
        HEALTH(9, float.class),
        POTION_EFFECT_COLOR(10, int.class),
        POTION_EFFECT_AMBIENT(11, boolean.class),
        ARROW_COUNT(12, int.class),
        BEE_STINGER_COUNT(13, int.class),
        SLEEPING_BED_LOCATION(14, BlockVector.class, EntityDataSerializerInfo.Type.OPTIONAL);

        private final int index;
        private final String name;
        private final EntityDataSerializerInfo<?> entityDataSerializerInfo;

        Property(int index, Class<?> type, EntityDataSerializerInfo.Type entityDataSerializerInfoType) {
            this.index = index;
            this.name = name().toLowerCase();
            this.entityDataSerializerInfo = new EntityDataSerializerInfo<>(type, entityDataSerializerInfoType);
        }

        Property(int index, Class<?> type) {
            this(index, type, EntityDataSerializerInfo.Type.NORMAL);
        }

        @Override
        public int getIndex() {
            return index;
        }

        @Override
        public Class<?> getType() {
            return entityDataSerializerInfo.tClass();
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public EntityDataSerializerInfo<?> getSerializerInfo() {
            return entityDataSerializerInfo;
        }
    }

    /**
     * Wrapper for the entity metadata bitmask at index 0.<br>
     * <a href="https://minecraft.wiki/w/Java_Edition_protocol/Entity_metadata#Living_Entity">Minecraft Wiki – Living Entity Metadata</a>
     */
    class HandStates extends Flags<HandStates.HandState> {

        public enum HandState implements Flag {
            HAND_ACTIVE((byte) 0x01),
            /**
             * 0 = main hand, 1 = offhand
             */
            HAND((byte) 0x02),
            RIPTIDE_ATTACK((byte) 0x04);

            private final byte mask;

            HandState(byte mask) {
                this.mask = mask;
            }

            @Override
            public byte getMask() {
                return mask;
            }
        }

        public HandStates() {
            flags = 0;
        }

        public HandStates(byte flags) {
            this.flags = flags;
        }

        public boolean isHandActive() {
            return getFlag(HandState.HAND_ACTIVE);
        }

        public void setHandActive(boolean value) {
            setFlag(HandState.HAND_ACTIVE, value);
        }

        public EquipmentSlot getHand() {
            return getFlag(HandState.HAND) ? EquipmentSlot.OFF_HAND : EquipmentSlot.HAND;
        }

        public void setHand(EquipmentSlot hand) {
            setFlag(HandState.HAND, hand == EquipmentSlot.OFF_HAND);
        }

        public boolean isRiptiding() {
            return getFlag(HandState.RIPTIDE_ATTACK);
        }

        public void setRiptiding(boolean value) {
            setFlag(HandState.RIPTIDE_ATTACK, value);
        }

        @SuppressWarnings("CloneDoesntCallSuperClone")
        @Override
        public Object clone() {
            return new HandStates(flags);
        }
    }

    default @NotNull HandStates getHandStates() {
        return new LivingEntityMetadata.HandStates();
    }

    default float getHealth() {
        return 1f;
    }

    default int getPotionEffectColor() {
        return 0;
    }

    default boolean isPotionEffectAmbient() {
        return false;
    }

    default int getArrowCount() {
        return 0;
    }

    default int getBeeStingerCount() {
        return 0;
    }

    default @NotNull Optional<BlockVector> getSleepingBedLocation() {
        return Optional.empty();
    }

    @Override
    @SuppressWarnings("unchecked")
    default HashMap<BaseProperty, AbstractEntityData<? extends Entity, ?, ?>> getEntityDataItems() {
        HashMap<BaseProperty, AbstractEntityData<? extends Entity, ?, ?>> map = EntityMetadataView.super.getEntityDataItems();
        map.put(HAND_STATES, DefaultEntityData.raw(8, getHandStates(), HandStates.class, Byte.class));
        map.put(HEALTH, DefaultEntityData.simple(9, getHealth()));
        map.put(POTION_EFFECT_COLOR, DefaultEntityData.simple(10, getPotionEffectColor()));
        map.put(POTION_EFFECT_AMBIENT, DefaultEntityData.simple(11, isPotionEffectAmbient()));
        map.put(ARROW_COUNT, DefaultEntityData.simple(12, getArrowCount()));
        map.put(BEE_STINGER_COUNT, DefaultEntityData.simple(13, getBeeStingerCount()));
        map.put(SLEEPING_BED_LOCATION, DefaultEntityData.rawOptional(14, getSleepingBedLocation().orElse(null), BlockVector.class, NMSAdapter.nmsAdapter.getNMSBlockVectorClass()));
        return map;
    }
}