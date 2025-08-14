package it.jakegblp.nms.api.entity.metadata.wrappers;

import it.jakegblp.nms.api.entity.metadata.Flags;
import org.bukkit.inventory.EquipmentSlot;

/**
 * Wrapper for the entity metadata bitmask at index 0.<br>
 * <a href="https://minecraft.wiki/w/Java_Edition_protocol/Entity_metadata#Living_Entity">Minecraft Wiki – Living Entity Metadata</a>
 */
public class HandStates extends Flags<HandState> {

    public HandStates() {
        super();
    }

    public HandStates(byte flags) {
        super(flags);
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

}