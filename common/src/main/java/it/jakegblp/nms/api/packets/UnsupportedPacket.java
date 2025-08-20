package it.jakegblp.nms.api.packets;

import it.jakegblp.nms.api.utils.Exceptionable;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public interface UnsupportedPacket extends Exceptionable<UnsupportedOperationException>, Packet {

}
