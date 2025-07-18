package it.jakegblp.nms.api.entity.metadata.entity;

import it.jakegblp.nms.api.entity.metadata.BitFlag;

public enum EntityFlag implements BitFlag {
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