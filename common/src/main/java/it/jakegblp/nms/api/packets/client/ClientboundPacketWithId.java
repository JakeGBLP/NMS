package it.jakegblp.nms.api.packets.client;

public interface ClientboundPacketWithId extends ClientboundPacket {
    int getEntityId();
}
