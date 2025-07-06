package it.jakegblp.nms.api;

import it.jakegblp.nms.NMS;
import it.jakegblp.nms.api.packets.Packet;
import org.bukkit.entity.Player;

public class NMSApi {

    private static int randomID;

    public static void sendPacket(Player player, Packet<?> packet) {
        NMSAdapter.NMS.asGenericNMSServerPlayer(player).sendPacket(packet.asNMS());
    }

    public static int generateRandomId() {
        randomID = NMS.RANDOM.nextInt();
        return randomID;
    }

    public static int getLastRandomID() {
        return randomID;
    }
}
