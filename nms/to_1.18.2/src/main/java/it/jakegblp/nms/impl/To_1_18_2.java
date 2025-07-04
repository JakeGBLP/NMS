package it.jakegblp.nms.impl;

import it.jakegblp.nms.api.EntitySpawnPacketAdapter;
import it.jakegblp.nms.api.packets.EntitySpawnPacket;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.world.entity.EntityType;

import static it.jakegblp.nms.api.NMSAdapter.nmsAdapter;
import static it.jakegblp.nms.api.utils.SharedUtils.asNMSVector;


public class To_1_18_2 implements EntitySpawnPacketAdapter<ClientboundAddEntityPacket> {

    @Override
    public ClientboundAddEntityPacket to(EntitySpawnPacket packet) {
        return new ClientboundAddEntityPacket(
                packet.getEntityId(),
                packet.getEntityUUID(),
                packet.getX(),
                packet.getY(),
                packet.getZ(),
                packet.getPitch(),
                packet.getYaw(),
                (EntityType<?>) nmsAdapter.entityTypeAdapter.from(packet.getEntityType()),
                packet.getData(),
                asNMSVector(packet.getVelocity()));
    }
}
