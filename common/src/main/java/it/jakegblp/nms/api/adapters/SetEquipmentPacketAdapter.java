package it.jakegblp.nms.api.adapters;

import it.jakegblp.nms.api.packets.client.SetEquipmentPacket;

public interface SetEquipmentPacketAdapter<
        NMSSetEquipmentPacket
        > {
    NMSSetEquipmentPacket toNMSSetEquipmentPacket(SetEquipmentPacket from);

    SetEquipmentPacket fromNMSSetEquipmentPacket(NMSSetEquipmentPacket from);

    Class<NMSSetEquipmentPacket> getNMSSetEquipmentPacketClass();

    default boolean isNMSSetEquipmentPacket(Object object) {
        return getNMSSetEquipmentPacketClass().isInstance(object);
    }
}
