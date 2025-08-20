package it.jakegblp.nms.api.adapters;

import it.jakegblp.nms.api.packets.client.EntityMetadataPacket;

public interface EntityMetadataPacketAdapter<NMSEntityMetadataPacket> {
    NMSEntityMetadataPacket toNMSEntityMetadataPacket(EntityMetadataPacket from);

    EntityMetadataPacket fromNMSEntityMetadataPacket(NMSEntityMetadataPacket from);

    Class<NMSEntityMetadataPacket> getNMSEntityMetadataPacketClass();

    default boolean isNMSEntityMetadataPacket(Object object) {
        return getNMSEntityMetadataPacketClass().isInstance(object);
    }
}