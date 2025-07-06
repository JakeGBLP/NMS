package it.jakegblp.nms.impl;

import it.jakegblp.nms.api.PlayerRotationPacketAdapter;
import it.jakegblp.nms.api.packets.PlayerRotationPacket;
import net.minecraft.network.protocol.game.ClientboundPlayerRotationPacket;

public class From_1_21_3 implements PlayerRotationPacketAdapter {

    @Override
    public ClientboundPlayerRotationPacket to(PlayerRotationPacket from) {
        return new ClientboundPlayerRotationPacket(from.getPitch(), from.getYaw());
    }
}