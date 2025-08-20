package it.jakegblp.nms.api.packets.client;

import it.jakegblp.nms.api.entity.metadata.EntityMetadata;
import it.jakegblp.nms.api.entity.metadata.MetadataKey;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.bukkit.entity.Entity;

import java.util.Map;

import static it.jakegblp.nms.api.AbstractNMS.NMS;

/**
 * <a href="https://minecraft.wiki/w/Java_Edition_protocol/Packets#Set_Entity_Metadata">Entity Metadata Packet</a>
 */
@Getter
@Setter
@ToString(callSuper = true)
@AllArgsConstructor
public final class EntityMetadataPacket implements ClientboundPacketWithId {
    private int entityId;
    private Class<? extends Entity> target;
    private EntityMetadata entityMetadata;

    public EntityMetadataPacket(
            int id,
            Class<? extends Entity> target,
            Map<MetadataKey<? extends Entity, ?>, Object> values
    ) {
        this(id, target, new EntityMetadata(values));
    }

    public EntityMetadataPacket(
            int id,
            Map<MetadataKey<? extends Entity, ?>, Object> values
    ) {
        this(id, Entity.class, values);
    }

    @Override
    public Object asNMS() {
        return NMS.toNMSEntityMetadataPacket(this);
    }
}