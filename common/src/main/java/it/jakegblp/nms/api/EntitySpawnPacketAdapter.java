package it.jakegblp.nms.api;

import it.jakegblp.nms.api.packets.EntitySpawnPacket;

public interface EntitySpawnPacketAdapter<P> {
    P to(EntitySpawnPacket packet);
}
