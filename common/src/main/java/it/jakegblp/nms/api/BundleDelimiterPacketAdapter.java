package it.jakegblp.nms.api;

import it.jakegblp.nms.api.packets.BundleDelimiterPacket;

public interface BundleDelimiterPacketAdapter<
        NMSBundleDelimiterPacket
        > {
    NMSBundleDelimiterPacket to(BundleDelimiterPacket from);
}
