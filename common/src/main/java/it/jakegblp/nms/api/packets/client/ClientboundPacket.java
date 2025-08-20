package it.jakegblp.nms.api.packets.client;

import it.jakegblp.nms.api.NMSApi;
import it.jakegblp.nms.api.packets.Packet;
import org.bukkit.entity.Player;

public interface ClientboundPacket extends Packet {

    default void send(Player player) {
        NMSApi.sendPacket(player, this);
    }

}
