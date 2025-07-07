package it.jakegblp.nms.api;

import it.jakegblp.nms.api.packets.Packet;
import org.bukkit.entity.Player;

public class NMSApi {

    public static void sendPacket(Player player, Packet<?> packet) {
        NMSAdapter.NMS.asGenericNMSServerPlayer(player).sendPacket(packet.asNMS());
    }
}
