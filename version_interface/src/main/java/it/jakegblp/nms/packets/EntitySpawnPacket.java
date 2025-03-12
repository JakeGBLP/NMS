package it.jakegblp.nms.packets;

import org.bukkit.entity.EntityType;
import org.bukkit.util.Vector;
import java.util.UUID;

public record EntitySpawnPacket(
        int entityId,
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

    public EntitySpawnPacket(
            int entityId,
            UUID entityUUID,
            double x,
            double y,
            double z,
            float pitch,
            float yaw,
            EntityType entityType,
            int data,
            Vector velocity) {
        this(entityId, entityUUID, x, y, z, pitch, yaw, entityType, data, velocity, 0);
    }

}
