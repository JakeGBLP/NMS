package it.jakegblp.nms.api.packets;

import it.jakegblp.nms.api.utils.NMSObject;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@ToString
@EqualsAndHashCode
@SuperBuilder
@NoArgsConstructor
public abstract class Packet implements NMSObject {
}
