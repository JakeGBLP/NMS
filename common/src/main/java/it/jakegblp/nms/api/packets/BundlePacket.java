package it.jakegblp.nms.api.packets;

import lombok.Getter;
import org.jspecify.annotations.NullMarked;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
@NullMarked
public abstract class BundlePacket<P extends Packet> implements Packet {

    private final List<P> packets = new ArrayList<>();

    public BundlePacket(List<? extends P> packets) {
        this.packets.addAll(packets);
    }

    /**
     * Add a single packet into this bundle
     */
    @SafeVarargs
    public final void addPackets(P... packet) {
        this.packets.addAll(List.of(packet));
    }

    /**
     * Add multiple packets at once
     */
    public void addPackets(Collection<? extends P> packets) {
        this.packets.addAll(packets);
    }
}
