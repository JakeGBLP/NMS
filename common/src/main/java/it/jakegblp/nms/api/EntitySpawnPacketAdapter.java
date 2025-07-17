package it.jakegblp.nms.api;

import it.jakegblp.nms.api.packets.EntitySpawnPacket;

public interface EntitySpawnPacketAdapter<
        NMSEntitySpawnPacket
        > {
    NMSEntitySpawnPacket toNMSEntitySpawnPacket(EntitySpawnPacket from);
}
