package it.jakegblp.nms.api.packets;

import it.jakegblp.nms.api.utils.Exceptionable;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public abstract class UnsupportedPacket extends Packet implements Exceptionable<UnsupportedOperationException> {

    public UnsupportedPacket() {
        validate();
    }

}
