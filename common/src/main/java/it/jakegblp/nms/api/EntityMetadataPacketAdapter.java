package it.jakegblp.nms.api;

import it.jakegblp.nms.api.packets.EntityMetadataPacket;

public interface EntityMetadataPacketAdapter<P> {
    P to(EntityMetadataPacket<?,?> to);
}
