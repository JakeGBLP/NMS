package it.jakegblp.nms.api.packets.client;

import it.jakegblp.nms.api.packets.BundlePacket;
import lombok.Getter;
import org.jspecify.annotations.NullMarked;

import java.util.List;

import static it.jakegblp.nms.api.AbstractNMS.NMS;

@Getter
@NullMarked
public class ClientBundlePacket extends BundlePacket<ClientboundPacket> implements ClientboundPacket {

    public ClientBundlePacket(List<? extends ClientboundPacket> packets) {
        super(packets);
    }

    @Override
    public Object asNMS() {
        return NMS.toNMSClientBundlePacket(this);
    }
}
