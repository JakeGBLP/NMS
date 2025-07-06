package it.jakegblp.nms.api.packets;

import it.jakegblp.nms.api.utils.Exceptionable;
import net.minecraft.network.PacketListener;
import net.minecraft.network.protocol.Packet;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public abstract class UnsupportedPacket implements Packet<PacketListener>, Exceptionable<UnsupportedOperationException> {

    public UnsupportedPacket() {
        validate();
    }

    @Override
    public void handle(PacketListener packetListener) {
        validate();
    }

}
