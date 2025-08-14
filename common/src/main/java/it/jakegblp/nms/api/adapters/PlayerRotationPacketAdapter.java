package it.jakegblp.nms.api.adapters;

import it.jakegblp.nms.api.packets.PlayerRotationPacket;

public interface PlayerRotationPacketAdapter {
    Object toNMSPlayerRotationPacket(PlayerRotationPacket from);

    PlayerRotationPacket fromNMSPlayerRotationPacket(Object from);

    Class<?> getNMSPlayerRotationPacketClass();

    default boolean isNMSPlayerRotationPacket(Object object) {
        return getNMSPlayerRotationPacketClass().isInstance(object);
    }
}
