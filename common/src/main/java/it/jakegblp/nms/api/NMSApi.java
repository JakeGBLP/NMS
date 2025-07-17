package it.jakegblp.nms.api;

import it.jakegblp.nms.api.packets.Packet;
import org.bukkit.entity.Player;

import static it.jakegblp.nms.api.NMSAdapter.NMS;

public class NMSApi {

    public static void sendPacket(Player player, Packet packet) {
        NMS.sendPacket(player, packet.asNMS());
    }
}
