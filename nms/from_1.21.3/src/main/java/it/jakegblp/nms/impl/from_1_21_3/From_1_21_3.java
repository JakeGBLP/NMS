package it.jakegblp.nms.impl.from_1_21_3;

import it.jakegblp.nms.api.adapters.PlayerRotationPacketAdapter;
import it.jakegblp.nms.api.packets.PlayerRotationPacket;
import net.minecraft.network.protocol.game.ClientboundPlayerRotationPacket;

public class From_1_21_3 implements PlayerRotationPacketAdapter {

    @Override
    public Object toNMSPlayerRotationPacket(PlayerRotationPacket from) {
        return new ClientboundPlayerRotationPacket(from.getPitch(), from.getYaw());
    }

    @Override
    public PlayerRotationPacket fromNMSPlayerRotationPacket(Object from) {
        ClientboundPlayerRotationPacket packet = (ClientboundPlayerRotationPacket) from;
        return new PlayerRotationPacket(packet.yRot(), packet.xRot());
    }

    @Override
    public Class<?> getNMSPlayerRotationPacketClass() {
        return ClientboundPlayerRotationPacket.class;
    }
}