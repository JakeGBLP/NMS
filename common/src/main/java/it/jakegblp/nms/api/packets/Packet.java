package it.jakegblp.nms.api.packets;

import it.jakegblp.nms.api.NMSObject;
import lombok.experimental.SuperBuilder;

@SuperBuilder
public abstract class Packet<P extends net.minecraft.network.protocol.Packet<?>> implements NMSObject<P> {
    Packet() {

    }

}
