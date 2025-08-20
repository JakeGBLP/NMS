package it.jakegblp.nms.impl.from_1_19;

import it.jakegblp.nms.api.adapters.EntitySpawnPacketAdapter;
import it.jakegblp.nms.api.packets.client.EntitySpawnPacket;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.phys.Vec3;
import org.bukkit.util.Vector;

import static it.jakegblp.nms.api.AbstractNMS.NMS;


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

    @Override
    public EntitySpawnPacket fromNMSEntitySpawnPacket(ClientboundAddEntityPacket from) {
        return new EntitySpawnPacket(
                from.getId(),
                from.getUUID(),
                from.getX(),
                from.getY(),
                from.getZ(),
                from.getXRot(),
                from.getYRot(),
                NMS.fromNMSEntityType(from.getType()),
                from.getData(),
                new Vector(from.getXa(), from.getYa(), from.getZa()),
                from.getYHeadRot()
        );
    }

    @Override
    public Class<ClientboundAddEntityPacket> getNMSEntitySpawnPacketClass() {
        return ClientboundAddEntityPacket.class;
    }
}
