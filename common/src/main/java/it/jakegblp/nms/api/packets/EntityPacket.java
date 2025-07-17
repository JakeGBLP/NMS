package it.jakegblp.nms.api.packets;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class EntityPacket extends Packet {
    protected int entityId;

    protected EntityPacket(int entityId) {
        super();
        this.entityId = entityId;
    }
}
