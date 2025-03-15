package it.jakegblp.nms;

import it.jakegblp.nms.api.packets.EntitySpawnPacket;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Nameable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftEntityType;
import org.bukkit.craftbukkit.v1_20_R3.util.CraftVector;

public final class v1_20_4 extends NMSAdapter<
        Nameable,
        Entity,
        LivingEntity,
        Player,
        ServerPlayer,
        EntityType<?>,
        Packet<?>,
        ClientboundAddEntityPacket
        > {

    @Override
    public EntityType<?> asNMSEntityType(org.bukkit.entity.EntityType entityType) {
        return CraftEntityType.bukkitToMinecraft(entityType);
    }

    @Override
    public ClientboundAddEntityPacket asNMSEntitySpawnPacket(EntitySpawnPacket packet) {
        return new ClientboundAddEntityPacket(
                packet.entityId(),
                packet.entityUUID(),
                packet.x(),
                packet.y(),
                packet.z(),
                packet.pitch(),
                packet.yaw(),
                asNMSEntityType(packet.entityType()),
                packet.data(),
                CraftVector.toNMS(packet.velocity()),
                packet.headYaw());
    }

    public class NMSServerPlayer extends GenericNMSServerPlayer {

        public NMSServerPlayer(org.bukkit.entity.Player player) {
            super(player);
        }

    }

}
