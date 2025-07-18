package it.jakegblp.nms.api.entity.metadata.entity;

import it.jakegblp.nms.api.entity.metadata.Flags;

/**
 * Wrapper for the entity metadata bitmask at index 0.<br>
 * <a href="https://minecraft.wiki/w/Java_Edition_protocol/Entity_metadata#Entity">Minecraft Wiki – Entity Metadata</a>
 */
public class EntityFlags extends Flags<EntityFlag> implements Cloneable {

    public EntityFlags() {
        super();
    }

    public EntityFlags(byte flags) {
        super(flags);
    }

    public boolean isOnFire() {
        return getFlag(EntityFlag.ON_FIRE);
    }

    public EntityFlags setOnFire(boolean value) {
        setFlag(EntityFlag.ON_FIRE, value);
        return this;
    }

    public boolean isSneaking() {
        return getFlag(EntityFlag.SNEAKING);
    }

    public EntityFlags setSneaking(boolean value) {
        setFlag(EntityFlag.SNEAKING, value);
        return this;
    }

    public boolean isUnused() {
        return getFlag(EntityFlag.UNUSED);
    }

    public EntityFlags setUnused(boolean value) {
        setFlag(EntityFlag.UNUSED, value);
        return this;
    }

    public boolean isSprinting() {
        return getFlag(EntityFlag.SPRINTING);
    }

    public EntityFlags setSprinting(boolean value) {
        setFlag(EntityFlag.SPRINTING, value);
        return this;
    }

    public boolean isSwimming() {
        return getFlag(EntityFlag.SWIMMING);
    }

    public EntityFlags setSwimming(boolean value) {
        setFlag(EntityFlag.SWIMMING, value);
        return this;
    }

    public boolean isInvisible() {
        return getFlag(EntityFlag.INVISIBLE);
    }

    public EntityFlags setInvisible(boolean value) {
        setFlag(EntityFlag.INVISIBLE, value);
        return this;
    }

    public boolean isGlowing() {
        return getFlag(EntityFlag.GLOWING);
    }

    public EntityFlags setGlowing(boolean value) {
        setFlag(EntityFlag.GLOWING, value);
        return this;
    }

    public boolean isFlyingElytra() {
        return getFlag(EntityFlag.FLYING_ELYTRA);
    }

    public EntityFlags setFlyingElytra(boolean value) {
        setFlag(EntityFlag.FLYING_ELYTRA, value);
        return this;
    }

    @SuppressWarnings("CloneDoesntCallSuperClone")
    @Override
    public EntityFlags clone() {
        return new EntityFlags(this.flags);
    }
}
