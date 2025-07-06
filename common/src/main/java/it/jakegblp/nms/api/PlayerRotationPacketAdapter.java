package it.jakegblp.nms.api;

import it.jakegblp.nms.api.packets.PlayerRotationPacket;

public interface PlayerRotationPacketAdapter {
    Object to(PlayerRotationPacket from);
}
