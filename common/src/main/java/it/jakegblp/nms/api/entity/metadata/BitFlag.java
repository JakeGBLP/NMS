package it.jakegblp.nms.api.entity.metadata;

/**
* Interface that all flag enums must implement.
* This is to ensure that the enum provides methods for checking and setting the flag.
*/
public interface BitFlag extends Cloneable {

    byte getMask();

    default boolean isSet(byte flags) {
        return (flags & getMask()) != 0;
    }

    default byte setFlag(byte flags, boolean value) {
        if (value)
            return (byte) (flags | getMask());
        else
            return (byte) (flags & ~getMask());
    }

}