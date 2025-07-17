package it.jakegblp.nms.impl.from_1_19;

import it.jakegblp.nms.api.EntitySpawnPacketAdapter;
import it.jakegblp.nms.api.packets.EntitySpawnPacket;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.phys.Vec3;

import static it.jakegblp.nms.api.NMSAdapter.NMS;


public class From_1_19 implements EntitySpawnPacketAdapter<
        ClientboundAddEntityPacket
        > {

    @Override
    @SuppressWarnings("ConstantConditions")
    public ClientboundAddEntityPacket toNMSEntitySpawnPacket(EntitySpawnPacket from) {
        return new ClientboundAddEntityPacket(
                from.getEntityId(),
                from.getEntityUUID(),
                from.getX(),
                from.getY(),
                from.getZ(),
                from.getPitch(),
                from.getYaw(),
                (EntityType<?>) NMS.toNMSEntityType(from.getEntityType()),
                from.getData(),
                (Vec3) NMS.asNMSVector(from.getVelocity()),
                from.getHeadYaw());
    }
}
