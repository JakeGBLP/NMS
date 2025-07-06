package it.jakegblp.nms.api;

import it.jakegblp.nms.api.packets.EntityMetadataPacket;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;

public interface EntityMetadataPacketAdapter {
    ClientboundSetEntityDataPacket to(EntityMetadataPacket<?,?> from);
}
