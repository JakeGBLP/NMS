package it.jakegblp.nms.api.packets;

import it.jakegblp.nms.api.entity.metadata.EntityMetadata;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import org.bukkit.entity.Entity;

import static it.jakegblp.nms.api.NMSAdapter.nmsAdapter;

/**
 * <a href="https://minecraft.wiki/w/Java_Edition_protocol/Packets#Set_Entity_Metadata">Entity Metadata Packet</a>
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = false)
public final class EntityMetadataPacket<M extends EntityMetadata, E extends Entity> extends EntityPacket<ClientboundSetEntityDataPacket> {
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
        super(id);
        this.target = target;
        this.entityMetadata = entityMetadata;
    }

    @Override
    @SuppressWarnings("unchecked")
    public ClientboundSetEntityDataPacket asNMS() {
        return (ClientboundSetEntityDataPacket) nmsAdapter.entityMetadataPacketAdapter.to(this);
    }
}
