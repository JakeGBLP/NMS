package it.jakegblp.nms.api.packets;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@ToString
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public abstract class ClientboundPacketWithId extends ClientboundPacket {
    protected int entityId;
}
