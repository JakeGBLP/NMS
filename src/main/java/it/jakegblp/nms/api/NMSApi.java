package it.jakegblp.nms.api;

import it.jakegblp.nms.NMS;
import it.jakegblp.nms.api.packets.Packet;
import lombok.Getter;
import org.bukkit.entity.Player;

public class NMSApi {

    @Getter
    private static int randomID;

    public static void sendPacket(Player player, Packet packet) {
        NMSAdapter.nmsAdapter.asGenericNMSServerPlayer(player).sendPacket(packet.asNMS());
    }

    public static int generateRandomId() {
        randomID = NMS.RANDOM.nextInt();
        return randomID;
    }

}
