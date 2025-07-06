package it.jakegblp.nms.impl;

import it.jakegblp.nms.api.EntitySpawnPacketAdapter;
import it.jakegblp.nms.api.packets.EntitySpawnPacket;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;

import static it.jakegblp.nms.api.NMSAdapter.NMS;
import static it.jakegblp.nms.api.utils.SharedUtils.asNMSVector;


public class To_1_18_2 implements EntitySpawnPacketAdapter {

    @Override
    public ClientboundAddEntityPacket to(EntitySpawnPacket from) {
        return new ClientboundAddEntityPacket(
                from.getEntityId(),
                from.getEntityUUID(),
                from.getX(),
                from.getY(),
                from.getZ(),
                from.getPitch(),
                from.getYaw(),
                NMS.entityTypeAdapter.from(from.getEntityType()),
                from.getData(),
                asNMSVector(from.getVelocity()));
    }
}
