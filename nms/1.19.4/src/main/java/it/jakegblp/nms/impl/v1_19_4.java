package it.jakegblp.nms.impl;

import it.jakegblp.nms.api.NMSAdapter;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.server.level.ServerPlayer;

public final class v1_19_4 extends NMSAdapter<
        ServerPlayer,
        EntityDataSerializer<?>,
        Packet<?>,
        ClientboundAddEntityPacket,
        ClientboundSetEntityDataPacket
        > {

    public class NMSServerPlayer extends GenericNMSServerPlayer {

        public NMSServerPlayer(org.bukkit.entity.Player player) {
            super(player);
        }

    }

}