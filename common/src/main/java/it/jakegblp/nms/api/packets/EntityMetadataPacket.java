package it.jakegblp.nms.api.packets;

import it.jakegblp.nms.api.entity.metadata.entity.EntityMetadata;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.bukkit.entity.Entity;

import static it.jakegblp.nms.api.NMSAdapter.NMS;

/**
 * <a href="https://minecraft.wiki/w/Java_Edition_protocol/Packets#Set_Entity_Metadata">Entity Metadata Packet</a>
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = false)
public final class EntityMetadataPacket<M extends EntityMetadata, E extends Entity> extends ClientboundPacket {
    private int entityId;
    private Class<E> target;
    private M entityMetadata;

    /**
     * @param id the id of the entity whose metadata should be changed
     * @param target the bukkit class of the targeted entity
     * @param entityMetadata the updated metadata
     */
    public EntityMetadataPacket(
            int id,
            Class<E> target,
            M entityMetadata
    ) {
        this.entityId = id;
        this.target = target;
        this.entityMetadata = entityMetadata;
    }

    @Override
    public Object asNMS() {
        return NMS.toNMSEntityMetadataPacket(this);
    }
}
