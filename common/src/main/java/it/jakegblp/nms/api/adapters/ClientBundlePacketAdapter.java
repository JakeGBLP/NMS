package it.jakegblp.nms.api.adapters;

import it.jakegblp.nms.api.packets.client.ClientBundlePacket;

public interface ClientBundlePacketAdapter<
        NMSClientBundlePacket
        > {
    NMSClientBundlePacket toNMSClientBundlePacket(ClientBundlePacket from);

    ClientBundlePacket fromNMSClientBundlePacket(NMSClientBundlePacket from);

    Class<NMSClientBundlePacket> getNMSClientBundlePacketClass();

    default boolean isNMSClientBundlePacket(Object object) {
        return getNMSClientBundlePacketClass().isInstance(object);
    }
}
