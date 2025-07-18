package it.jakegblp.nms.impl.from_1_19_4;

import it.jakegblp.nms.api.BundleDelimiterPacketAdapter;
import it.jakegblp.nms.api.packets.BundleDelimiterPacket;
import it.jakegblp.nms.api.packets.ClientboundPacket;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBundlePacket;

import java.util.ArrayList;
import java.util.List;

public class From_1_19_4 implements BundleDelimiterPacketAdapter<ClientboundBundlePacket> {

    @Override
    @SuppressWarnings("unchecked")
    public ClientboundBundlePacket to(BundleDelimiterPacket from) {
        List<Packet<ClientGamePacketListener>> nmsPackets =  new ArrayList<>();
        for (ClientboundPacket packet : from.getPackets())
            nmsPackets.add((Packet<ClientGamePacketListener>) packet.asNMS());
        return new ClientboundBundlePacket(nmsPackets);
    }
}