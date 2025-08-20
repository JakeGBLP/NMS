package it.jakegblp.nms.api.packets.client;

import lombok.*;
import org.bukkit.entity.EntityType;
import org.bukkit.util.Vector;

import java.util.UUID;

import static it.jakegblp.nms.api.AbstractNMS.NMS;

/**
 * <a href="https://minecraft.wiki/w/Java_Edition_protocol/Packets#Spawn_Entity">Entity Spawn Packet</a>
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
public class EntitySpawnPacket implements ClientboundPacketWithId {

    protected int entityId;
    protected UUID entityUUID;
    protected double x;
    protected double y;
    protected double z;
    protected float pitch;
    protected float yaw;
    protected EntityType entityType;
    protected int data;
    protected Vector velocity;
    protected double headYaw;

    public EntitySpawnPacket(
            int id,
            UUID entityUUID,
            double x,
            double y,
            double z,
            float pitch,
            float yaw,
            EntityType entityType,
            int data,
            Vector velocity) {
        this(id, entityUUID, x, y, z, pitch, yaw, entityType, data, velocity, 0);
    }

    @Override
    public Object asNMS() {
        return NMS.toNMSEntitySpawnPacket(this);
    }
}
