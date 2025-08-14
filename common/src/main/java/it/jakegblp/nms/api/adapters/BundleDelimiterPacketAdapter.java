package it.jakegblp.nms.api.adapters;

import it.jakegblp.nms.api.packets.BundleDelimiterPacket;

public interface BundleDelimiterPacketAdapter<
        NMSBundleDelimiterPacket
        > {
    NMSBundleDelimiterPacket toNMSBundleDelimiterPacket(BundleDelimiterPacket from);

    BundleDelimiterPacket fromNMSBundleDelimiterPacket(NMSBundleDelimiterPacket from);

    Class<NMSBundleDelimiterPacket> getNMSBundleDelimiterPacketClass();

    default boolean isNMSBundleDelimiterPacket(Object object) {
        return getNMSBundleDelimiterPacketClass().isInstance(object);
    }
}
