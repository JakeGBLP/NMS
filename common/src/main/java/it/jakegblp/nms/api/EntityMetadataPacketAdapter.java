package it.jakegblp.nms.api;

import it.jakegblp.nms.api.packets.EntityMetadataPacket;

public interface EntityMetadataPacketAdapter<NMSEntityMetadataPacket> {
    NMSEntityMetadataPacket toNMSEntityMetadataPacket(EntityMetadataPacket<?,?> from);
}
