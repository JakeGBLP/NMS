package it.jakegblp.nms.impl.from_1_19_4;

import it.jakegblp.nms.api.adapters.BundleDelimiterPacketAdapter;
import it.jakegblp.nms.api.packets.BundleDelimiterPacket;
import it.jakegblp.nms.api.packets.ClientboundPacket;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBundlePacket;

import java.util.ArrayList;
import java.util.List;

import static it.jakegblp.nms.api.AbstractNMS.NMS;

public class From_1_19_4 implements BundleDelimiterPacketAdapter<ClientboundBundlePacket> {

    @Override
    @SuppressWarnings("unchecked")
    public ClientboundBundlePacket toNMSBundleDelimiterPacket(BundleDelimiterPacket from) {
        List<Packet<ClientGamePacketListener>> nmsPackets = new ArrayList<>();
        for (ClientboundPacket packet : from.getPackets())
            nmsPackets.add((Packet<ClientGamePacketListener>) packet.asNMS());
        return new ClientboundBundlePacket(nmsPackets);
    }

    @Override
    public BundleDelimiterPacket fromNMSBundleDelimiterPacket(ClientboundBundlePacket from) {
        List<ClientboundPacket> packetList = new ArrayList<>();
        for (Packet<ClientGamePacketListener> subPacket : from.subPackets()) {
            it.jakegblp.nms.api.packets.Packet packet = NMS.fromNMSPacket(subPacket);
            if (packet instanceof ClientboundPacket clientboundPacket)
                packetList.add(clientboundPacket);
        }
        return new BundleDelimiterPacket(packetList);
    }

    @Override
    public Class<ClientboundBundlePacket> getNMSBundleDelimiterPacketClass() {
        return ClientboundBundlePacket.class;
    }
}