package it.jakegblp.nms.api.adapters;

import it.jakegblp.nms.api.packets.client.EntitySpawnPacket;

public interface EntitySpawnPacketAdapter<
        NMSEntitySpawnPacket
        > {
    NMSEntitySpawnPacket toNMSEntitySpawnPacket(EntitySpawnPacket from);

    EntitySpawnPacket fromNMSEntitySpawnPacket(NMSEntitySpawnPacket from);

    Class<NMSEntitySpawnPacket> getNMSEntitySpawnPacketClass();

    default boolean isNMSEntitySpawnPacket(Object object) {
        return getNMSEntitySpawnPacketClass().isInstance(object);
    }
}
