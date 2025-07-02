package it.jakegblp.nms.api.packets;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public abstract class EntityPacket<P extends net.minecraft.network.protocol.Packet<?>> extends Packet<P> {
    protected int entityId;

    protected EntityPacket(int entityId) {
        super();
        this.entityId = entityId;
    }
}
