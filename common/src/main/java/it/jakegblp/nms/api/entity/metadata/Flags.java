package it.jakegblp.nms.api.entity.metadata;

import it.jakegblp.nms.api.NMSObject;

/**
 * Abstract base class for handling entity flags, which works with any enum that represents flag values.
 * The subclass must specify the enum type.
 */
public abstract class Flags<T extends Enum<T> & Flag> implements NMSObject<Byte> {

    protected byte flags;

    public Flags() {
        flags = 0;
    }

    public Flags(byte flags) {
        this.flags = flags;
    }

    /**
     * Generic method to get the value of a flag.
     *
     * @param flag The flag to check
     * @return True if the flag is set, false otherwise
     */
    public boolean getFlag(T flag) {
        return flag.isSet(flags);
    }

    /**
     * Generic method to set the value of a flag.
     *
     * @param flag The flag to set or clear
     * @param value The value to set (true to set, false to clear)
     */
    public void setFlag(T flag, boolean value) {
        flags = flag.setFlag(flags, value);
    }

    @Override
    public Byte asNMS() {
        return flags;
    }
}
