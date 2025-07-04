package it.jakegblp.nms.api.packets;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import org.bukkit.entity.EntityType;
import org.bukkit.util.Vector;

import java.util.Objects;
import java.util.UUID;

import static it.jakegblp.nms.api.NMSAdapter.nmsAdapter;

/**
 * <a href="https://minecraft.wiki/w/Java_Edition_protocol/Packets#Spawn_Entity">Entity Spawn Packet</a>
 */
@Getter
@Setter
public class EntitySpawnPacket extends EntityPacket<ClientboundAddEntityPacket> {

    private UUID entityUUID;
    private double x;
    private double y;
    private double z;
    private float pitch;
    private float yaw;
    private EntityType entityType;
    private int data;
    private Vector velocity;
    private double headYaw;

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
            Vector velocity,
            double headYaw) {
        super(id);
        this.entityUUID = entityUUID;
        this.x = x;
        this.y = y;
        this.z = z;
        this.pitch = pitch;
        this.yaw = yaw;
        this.entityType = entityType;
        this.data = data;
        this.velocity = velocity;
        this.headYaw = headYaw;
    }

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
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (EntitySpawnPacket) obj;
        return this.entityId == that.entityId &&
                Objects.equals(this.entityUUID, that.entityUUID) &&
                Double.doubleToLongBits(this.x) == Double.doubleToLongBits(that.x) &&
                Double.doubleToLongBits(this.y) == Double.doubleToLongBits(that.y) &&
                Double.doubleToLongBits(this.z) == Double.doubleToLongBits(that.z) &&
                Float.floatToIntBits(this.pitch) == Float.floatToIntBits(that.pitch) &&
                Float.floatToIntBits(this.yaw) == Float.floatToIntBits(that.yaw) &&
                Objects.equals(this.entityType, that.entityType) &&
                this.data == that.data &&
                Objects.equals(this.velocity, that.velocity) &&
                Double.doubleToLongBits(this.headYaw) == Double.doubleToLongBits(that.headYaw);
    }

    @Override
    public int hashCode() {
        return Objects.hash(entityId, entityUUID, x, y, z, pitch, yaw, entityType, data, velocity, headYaw);
    }

    @Override
    public String toString() {
        return "EntitySpawnPacket[" +
                "id=" + entityId + ", " +
                "entityUUID=" + entityUUID + ", " +
                "x=" + x + ", " +
                "y=" + y + ", " +
                "z=" + z + ", " +
                "pitch=" + pitch + ", " +
                "yaw=" + yaw + ", " +
                "entityType=" + entityType + ", " +
                "data=" + data + ", " +
                "velocity=" + velocity + ", " +
                "headYaw=" + headYaw + ']';
    }


    @Override
    public ClientboundAddEntityPacket asNMS() {
        return (ClientboundAddEntityPacket) nmsAdapter.entitySpawnPacketAdapter.to(this);
    }
}
