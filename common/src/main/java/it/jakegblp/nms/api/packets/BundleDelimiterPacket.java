package it.jakegblp.nms.api.packets;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

import static it.jakegblp.nms.api.AbstractNMS.NMS;

@Getter
@Setter
public class BundleDelimiterPacket extends ClientboundPacket {

    private List<ClientboundPacket> packets;

    public BundleDelimiterPacket(List<ClientboundPacket> packets) {
        this.packets = packets;
    }


    @Override
    public Object asNMS() {
        return NMS.bundleDelimiterPacketAdapter.toNMSBundleDelimiterPacket(this);
    }
}
