package it.jakegblp.nms.api;

import it.jakegblp.nms.api.packets.Packet;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

import static it.jakegblp.nms.api.AbstractNMS.NMS;

public class NMSApi {

    public static final Random RANDOM = new Random();
    private static int randomID;

    public static int generateRandomId() {
        randomID = RANDOM.nextInt();
        return randomID;
    }

    public static int getLastRandomID() {
        return randomID;
    }

    public static void sendPacket(Player player, Packet packet) {
        NMS.sendPacket(player, packet.asNMS());
    }

    public static Object asNMSEquipmentSlot(EquipmentSlot equipmentSlot) {
        return NMS.asNMSEquipmentSlot(equipmentSlot);
    }

    public static EquipmentSlot asEquipmentSlot(Enum<?> nmsEquipmentSlot) {
        return NMS.asEquipmentSlot(nmsEquipmentSlot);
    }

    public static Object asNMSItemStack(ItemStack itemStack) {
        return NMS.asNMSItemStack(itemStack);
    }

    public static ItemStack asItemStack(Object nmsItemStack) {
        return NMS.asItemStack(nmsItemStack);
    }


}
