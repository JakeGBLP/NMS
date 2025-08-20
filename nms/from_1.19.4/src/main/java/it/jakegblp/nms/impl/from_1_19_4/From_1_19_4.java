package it.jakegblp.nms.impl.from_1_19_4;

import it.jakegblp.nms.api.adapters.ClientBundlePacketAdapter;
import it.jakegblp.nms.api.packets.client.ClientBundlePacket;
import it.jakegblp.nms.api.packets.client.ClientboundPacket;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBundlePacket;

import java.util.ArrayList;
import java.util.List;

import static it.jakegblp.nms.api.AbstractNMS.NMS;

public class From_1_19_4 implements ClientBundlePacketAdapter<ClientboundBundlePacket> {

    @Override
    @SuppressWarnings("unchecked")
    public ClientboundBundlePacket toNMSClientBundlePacket(ClientBundlePacket from) {
        List<Packet<ClientGamePacketListener>> nmsPackets = new ArrayList<>();
        for (ClientboundPacket packet : from.getPackets())
            nmsPackets.add((Packet<ClientGamePacketListener>) packet.asNMS());
        return new ClientboundBundlePacket(nmsPackets);
    }

    @Override
    public ClientBundlePacket fromNMSClientBundlePacket(ClientboundBundlePacket from) {
        List<ClientboundPacket> packetList = new ArrayList<>();
        for (Packet<ClientGamePacketListener> subPacket : from.subPackets()) {
            it.jakegblp.nms.api.packets.Packet packet = NMS.fromNMSPacket(subPacket);
            if (packet instanceof ClientboundPacket clientboundPacket)
                packetList.add(clientboundPacket);
        }
        return new ClientBundlePacket(packetList);
    }

    @Override
    public Class<ClientboundBundlePacket> getNMSClientBundlePacketClass() {
        return ClientboundBundlePacket.class;
    }
}