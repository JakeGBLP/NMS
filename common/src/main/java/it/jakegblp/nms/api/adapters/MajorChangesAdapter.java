package it.jakegblp.nms.api.adapters;

import com.google.common.collect.BiMap;
import io.netty.channel.*;
import it.jakegblp.nms.api.events.PacketReceiveEvent;
import it.jakegblp.nms.api.events.PacketSendEvent;
import it.jakegblp.nms.api.packets.BlockDestructionPacket;
import it.jakegblp.nms.api.packets.ClientboundPacket;
import it.jakegblp.nms.api.packets.Packet;
import it.jakegblp.nms.api.packets.ServerboundPacket;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.entity.Pose;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.BlockVector;
import org.bukkit.util.Vector;

import static it.jakegblp.nms.api.AbstractNMS.NMS;
import static it.jakegblp.nms.api.utils.ReflectionUtils.*;

public interface MajorChangesAdapter<
        NMSVector,
        NMSBlockPos,
        NMSServerPlayer,
        NMSPose,
        NMSComponent,
        NMSPacket,
        NMSServerGamePacketListenerImpl,
        NMSConnection,
        NMSBlockDestructionPacket
        > {
    String CRAFT_BUKKIT_PACKAGE = Bukkit.getServer().getClass().getPackage().getName();

    Class<NMSPacket> getNMSPacketClass();

    default boolean isNMSPacket(Object object) {
        return getNMSPacketClass().isInstance(object);
    }

    BiMap<Pose, NMSPose> getPoseMap();

    Vector asVector(NMSVector nmsVector);

    NMSVector asNMSVector(Vector vector);

    Class<NMSVector> getNMSVectorClass();

    BlockVector asBlockVector(NMSBlockPos blockPosition);

    NMSBlockPos asNMSBlockVector(BlockVector blockVector);

    Class<NMSBlockPos> getNMSBlockVectorClass();

    Pose asPose(NMSPose nmsPose);

    NMSPose asNMSPose(Pose pose);

    Class<NMSPose> getNMSPoseClass();

    Player asPlayer(NMSServerPlayer serverPlayer);

    @SuppressWarnings("unchecked")
    default NMSServerPlayer asServerPlayer(Player player) {
        return (NMSServerPlayer) invokeSafely(getDeclaredMethod(getCraftPlayerClass(), "getHandle"), player);
    }

    Class<NMSServerPlayer> getNMSServerPlayerClass();

    @SuppressWarnings("unchecked")
    default Class<? extends Player> getCraftPlayerClass() {
        return (Class<? extends Player>) forClassName(CRAFT_BUKKIT_PACKAGE + ".entity.CraftPlayer");
    }

    NMSServerGamePacketListenerImpl getPlayerConnection(NMSServerPlayer serverPlayer);

    default NMSServerGamePacketListenerImpl getPlayerConnection(Player player) {
        return getPlayerConnection(asServerPlayer(player));
    }

    NMSBlockDestructionPacket toNMSBlockDestructionPacket(BlockDestructionPacket from);

    BlockDestructionPacket fromNMSBlockDestructionPacket(NMSBlockDestructionPacket from);

    Class<NMSBlockDestructionPacket> getNMSBlockDestructionPacketClass();

    default boolean isNMSBlockDestructionPacket(Object object) {
        return getNMSBlockDestructionPacketClass().isInstance(object);
    }

    NMSComponent asNMSComponent(Component component);

    Component asComponent(NMSComponent nmsComponent);

    Class<NMSComponent> getNMSComponentClass();

    default void sendPacket(Player player, NMSPacket packet) {
        sendPacket(asServerPlayer(player), packet);
    }

    default void sendPacket(NMSServerPlayer player, NMSPacket packet) {
        sendPacketInternal(getPlayerConnection(player), packet);
    }

    void sendPacketInternal(NMSServerGamePacketListenerImpl packetListener, NMSPacket packet);

    NMSConnection getConnection(NMSServerGamePacketListenerImpl packetListener);

    Channel getChannel(NMSConnection connection);

    default void uninjectPlayer(Player player) {
        uninjectPlayer(asServerPlayer(player));
    }

    default void uninjectPlayer(NMSServerPlayer serverPlayer) {
        ChannelPipeline pipeline = getChannel(getConnection(getPlayerConnection(serverPlayer))).pipeline();
        if (pipeline.get("packet_interceptor") != null) {
            pipeline.remove("packet_interceptor");
        }
    }

    default void injectPlayer(Player player, JavaPlugin plugin) {
        injectPlayer(asServerPlayer(player), plugin);
    }

    default void injectPlayer(NMSServerPlayer serverPlayer, JavaPlugin plugin) {
        ChannelPipeline pipeline = getChannel(getConnection(getPlayerConnection(serverPlayer))).pipeline();
        if (pipeline.get("packet_interceptor") != null) return;

        pipeline.addBefore("packet_handler", "packet_interceptor", new ChannelDuplexHandler() {

            @Override
            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                if (!ctx.channel().isActive()) return; // Player already disconnected
                if (!getNMSPacketClass().isInstance(msg)) {
                    super.channelRead(ctx, msg);
                    return;
                }
                Bukkit.getScheduler().runTask(plugin, () -> {
                    if (!ctx.channel().isActive()) return; // Re-check on Bukkit thread
                    ServerboundPacket packet = (ServerboundPacket) NMS.fromNMSPacket(msg);
                    PacketReceiveEvent event = new PacketReceiveEvent(packet);
                    Bukkit.getPluginManager().callEvent(event);
                    if (event.isCancelled()) return;
                    Packet newPacket = event.getPacket();
                    ctx.executor().execute(() -> {
                        if (!ctx.channel().isActive()) return;
                        try {
                            super.channelRead(ctx, newPacket != packet ? newPacket : msg);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                });
            }

            @Override
            public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                if (!ctx.channel().isActive()) {
                    promise.setSuccess(); // No-op if channel is closed
                    return;
                }
                if (!getNMSPacketClass().isInstance(msg)) {
                    super.write(ctx, msg, promise);
                    return;
                }
                Bukkit.getScheduler().runTask(plugin, () -> {
                    if (!ctx.channel().isActive()) {
                        promise.setSuccess();
                        return;
                    }
                    ClientboundPacket packet = (ClientboundPacket) NMS.fromNMSPacket(msg);
                    PacketSendEvent event = new PacketSendEvent(packet);
                    Bukkit.getPluginManager().callEvent(event);
                    if (event.isCancelled()) {
                        promise.setSuccess();
                        return;
                    }
                    Packet newPacket = event.getPacket();
                    ctx.executor().execute(() -> {
                        if (!ctx.channel().isActive()) {
                            promise.setSuccess();
                            return;
                        }
                        try {
                            super.write(ctx, newPacket != packet ? newPacket : msg, promise);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                });
            }

            @Override
            public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
                // Ignore disconnect-related noise
                if (cause instanceof java.nio.channels.ClosedChannelException) {
                    return;
                }
                cause.printStackTrace();
                super.exceptionCaught(ctx, cause);
            }
        });


    }


}