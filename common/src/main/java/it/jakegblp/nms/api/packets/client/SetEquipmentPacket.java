package it.jakegblp.nms.api.packets.client;

import it.jakegblp.nms.api.annotations.SinceMinecraft;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

import static it.jakegblp.nms.api.AbstractNMS.NMS;

@Getter
@Setter
public class SetEquipmentPacket implements ClientboundPacketWithId {

    private int entityId;
    @SinceMinecraft(version = "1.20.5")
    private boolean sanitized;
    private Map<EquipmentSlot, ItemStack> equipment;

    @SinceMinecraft(version = "1.20.5")
    public SetEquipmentPacket(int entityId, @SinceMinecraft(version = "1.20.5") boolean sanitized, Map<EquipmentSlot, ItemStack> equipment) {
        this.entityId = entityId;
        this.sanitized = sanitized;
        this.equipment = equipment;
    }

    @SinceMinecraft(version = "1.20.5")
    public boolean isSanitized() {
        return sanitized;
    }

    @SinceMinecraft(version = "1.20.5")
    public SetEquipmentPacket(int entityId, boolean sanitized) {
        this(entityId, sanitized, Map.of());
    }

    public SetEquipmentPacket(int entityId, Map<EquipmentSlot, ItemStack> equipment) {
        this(entityId, false, equipment);
    }

    public SetEquipmentPacket(int entityId) {
        this(entityId, false, Map.of());
    }

    public void setEquipment(EquipmentSlot slot, ItemStack item) {
        equipment.put(slot, item);
    }

    public void removeEquipment(EquipmentSlot slot) {
        equipment.remove(slot);
    }

    public void clearEquipment() {
        equipment.clear();
    }

    @Override
    public Object asNMS() {
        return NMS.toNMSSetEquipmentPacket(this);
    }
}
