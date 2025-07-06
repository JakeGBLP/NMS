package it.jakegblp.nms.api;

import it.jakegblp.nms.api.packets.EntitySpawnPacket;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;

public interface EntitySpawnPacketAdapter {
    ClientboundAddEntityPacket to(EntitySpawnPacket from);
}
