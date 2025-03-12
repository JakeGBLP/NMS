package it.jakegblp.nms;

import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Nameable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public final class v1_17_1 extends NMSAdapter<
        Nameable,
        Entity,
        LivingEntity,
        Player,
        ServerPlayer,
        Packet<?>,
        ClientboundAddEntityPacket
        > {

    public class NMSServerPlayer extends GenericNMSServerPlayer {

        public NMSServerPlayer(org.bukkit.entity.Player player) {
            super(player);
        }

    }

}
