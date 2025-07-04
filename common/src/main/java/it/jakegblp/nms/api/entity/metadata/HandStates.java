package it.jakegblp.nms.api.entity.metadata;

import org.bukkit.inventory.EquipmentSlot;

/**
 * Wrapper for the entity metadata bitmask at index 0.<br>
 * <a href="https://minecraft.wiki/w/Java_Edition_protocol/Entity_metadata#Living_Entity">Minecraft Wiki – Living Entity Metadata</a>
 */
public class HandStates extends Flags<HandStates.HandState> {

    public HandStates() {
        flags = 0;
    }

    public HandStates(byte flags) {
        this.flags = flags;
    }

    public boolean isHandActive() {
        return getFlag(HandState.HAND_ACTIVE);
    }

    public HandStates setHandActive(boolean value) {
        setFlag(HandState.HAND_ACTIVE, value);
        return this;
    }

    public EquipmentSlot getHand() {
        return getFlag(HandState.HAND) ? EquipmentSlot.OFF_HAND : EquipmentSlot.HAND;
    }

    public HandStates setHand(EquipmentSlot hand) {
        setFlag(HandState.HAND, hand == EquipmentSlot.OFF_HAND);
        return this;
    }

    public boolean isRiptiding() {
        return getFlag(HandState.RIPTIDE_ATTACK);
    }

    public HandStates setRiptiding(boolean value) {
        setFlag(HandState.RIPTIDE_ATTACK, value);
        return this;
    }

    @SuppressWarnings("CloneDoesntCallSuperClone")
    @Override
    public HandStates clone() {
        return new HandStates(flags);
    }

    public enum HandState implements BitFlag {
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
}