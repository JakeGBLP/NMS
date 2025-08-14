package it.jakegblp.nms.api.packets;

import it.jakegblp.nms.api.entity.metadata.EntityMetadata;
import it.jakegblp.nms.api.entity.metadata.MetadataKey;
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
public final class EntityMetadataPacket extends ClientboundPacketWithId {
    private Class<? extends Entity> target;
    private EntityMetadata entityMetadata;

    /**
     * @param id             the id of the entity whose metadata should be changed
     * @param target         the bukkit class of the targeted entity
     * @param entityMetadata the updated metadata
     */
    public EntityMetadataPacket(
            int id,
            Class<? extends Entity> target,
            EntityMetadata entityMetadata
    ) {
        super(id);
        this.target = target;
        this.entityMetadata = entityMetadata;
    }

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