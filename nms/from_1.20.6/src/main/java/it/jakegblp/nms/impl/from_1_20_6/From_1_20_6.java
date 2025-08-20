package it.jakegblp.nms.impl.from_1_20_6;


import com.mojang.datafixers.util.Pair;
import it.jakegblp.nms.api.NMSApi;
import it.jakegblp.nms.api.adapters.SetEquipmentPacketAdapter;
import it.jakegblp.nms.api.packets.client.SetEquipmentPacket;
import net.minecraft.network.protocol.game.ClientboundSetEquipmentPacket;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;

import java.util.List;

import static it.jakegblp.nms.api.utils.StructureTranslation.fromMapToPairList;
import static it.jakegblp.nms.api.utils.StructureTranslation.fromPairListToMap;

public class From_1_20_6 implements SetEquipmentPacketAdapter<ClientboundSetEquipmentPacket> {

    @Override
    @SuppressWarnings("unchecked")
    public ClientboundSetEquipmentPacket toNMSSetEquipmentPacket(SetEquipmentPacket from) {
        Object list = fromMapToPairList(from.getEquipment(), NMSApi::asNMSEquipmentSlot, NMSApi::asNMSItemStack);
        return new ClientboundSetEquipmentPacket(from.getEntityId(), (List<Pair<EquipmentSlot, ItemStack>>) list);
    }

    @Override
    public SetEquipmentPacket fromNMSSetEquipmentPacket(ClientboundSetEquipmentPacket from) {
        return new SetEquipmentPacket(from.getEntity(), fromPairListToMap(from.getSlots(), NMSApi::asEquipmentSlot, NMSApi::asItemStack));
    }

    @Override
    public Class<ClientboundSetEquipmentPacket> getNMSSetEquipmentPacketClass() {
        return ClientboundSetEquipmentPacket.class;
    }}