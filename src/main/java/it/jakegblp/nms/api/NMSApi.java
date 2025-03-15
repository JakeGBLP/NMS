package it.jakegblp.nms.api;

import it.jakegblp.nms.NMS;
import it.jakegblp.nms.api.packets.EntitySpawnPacket;
import org.bukkit.entity.Player;

public class NMSApi {

    public static void sendEntitySpawnPacket(Player player, EntitySpawnPacket packet) {
        NMS.getInstance().getNmsAdapter().asGenericNMSServerPlayer(player).sendEntitySpawnPacket(packet);
    }

}
