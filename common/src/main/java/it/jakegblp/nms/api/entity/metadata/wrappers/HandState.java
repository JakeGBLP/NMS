package it.jakegblp.nms.api.entity.metadata.wrappers;

import it.jakegblp.nms.api.entity.metadata.BitFlag;

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