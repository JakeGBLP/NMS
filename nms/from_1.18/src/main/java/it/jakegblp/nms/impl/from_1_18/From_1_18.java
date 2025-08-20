package it.jakegblp.nms.impl.from_1_18;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import io.netty.channel.Channel;
import it.jakegblp.nms.api.adapters.MajorChangesAdapter;
import it.jakegblp.nms.api.packets.client.BlockDestructionPacket;
import lombok.Getter;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundBlockDestructionPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.bukkit.entity.Player;
import org.bukkit.util.BlockVector;
import org.bukkit.util.Vector;

@Getter
public class From_1_18 implements
        MajorChangesAdapter<EquipmentSlot, ItemStack, Vec3, BlockPos, ServerPlayer, Pose, Component, Packet, ServerGamePacketListenerImpl, Connection, ClientboundBlockDestructionPacket> {

    private final BiMap<org.bukkit.entity.Pose, Pose> poseMap;

    public From_1_18() {
        poseMap = ImmutableBiMap.of(org.bukkit.entity.Pose.SNEAKING, Pose.CROUCHING);
    }

    @Override
    public Class<Packet> getNMSPacketClass() {
        return Packet.class;
    }

    @Override
    public Class<ItemStack> getNMSItemStackClass() {
        return ItemStack.class;
    }

    @Override
    public Class<EquipmentSlot> getNMSEquipmentSlotClass() {
        return EquipmentSlot.class;
    }

    @Override
    public Vector asVector(Vec3 vec3) {
        return new Vector(vec3.x, vec3.y, vec3.z);
    }

    @Override
    public Vec3 asNMSVector(Vector vector) {
        return new Vec3(vector.getX(), vector.getY(), vector.getZ());
    }

    @Override
    public Class<Vec3> getNMSVectorClass() {
        return Vec3.class;
    }

    @Override
    public BlockVector asBlockVector(BlockPos blockPosition) {
        return new BlockVector(blockPosition.getX(), blockPosition.getY(), blockPosition.getZ());
    }

    @Override
    public BlockPos asNMSBlockVector(BlockVector blockVector) {
        return new BlockPos(blockVector.getBlockX(), blockVector.getBlockY(), blockVector.getBlockZ());
    }

    @Override
    public Class<BlockPos> getNMSBlockVectorClass() {
        return BlockPos.class;
    }

    @Override
    public org.bukkit.entity.Pose asPose(Pose pose) {
        return getPoseMap().inverse().getOrDefault(pose, org.bukkit.entity.Pose.valueOf(pose.toString()));
    }

    @Override
    public Pose asNMSPose(org.bukkit.entity.Pose pose) {
        return getPoseMap().getOrDefault(pose, Pose.valueOf(pose.toString()));
    }

    @Override
    public Class<Pose> getNMSPoseClass() {
        return Pose.class;
    }

    @Override
    public Player asPlayer(ServerPlayer serverPlayer) {
        return serverPlayer.getBukkitEntity();
    }

    @Override
    public Class<ServerPlayer> getNMSServerPlayerClass() {
        return ServerPlayer.class;
    }

    @Override
    public ServerGamePacketListenerImpl getPlayerConnection(ServerPlayer serverPlayer) {
        return serverPlayer.connection;
    }

    @Override
    public Class<Component> getNMSComponentClass() {
        return Component.class;
    }

    @Override
    public void sendPacketInternal(ServerGamePacketListenerImpl serverGamePacketListener, Packet packet) {
        serverGamePacketListener.send(packet);
    }

    @Override
    public Connection getConnection(ServerGamePacketListenerImpl serverGamePacketListener) {
        return serverGamePacketListener.connection;
    }

    @Override
    public Channel getChannel(Connection connection) {
        return connection.channel;
    }

    @Override
    public Component asNMSComponent(net.kyori.adventure.text.Component component) {
        return Component.Serializer.fromJson(GsonComponentSerializer.gson().serialize(component));
    }

    @Override
    public net.kyori.adventure.text.Component asComponent(Component component) {
        return GsonComponentSerializer.gson().deserialize(Component.Serializer.toJson(component));
    }

    @Override
    public ClientboundBlockDestructionPacket toNMSBlockDestructionPacket(BlockDestructionPacket from) {
        return new ClientboundBlockDestructionPacket(from.getEntityId(), asNMSBlockVector(from.getPosition()), from.getBlockDestructionStage());
    }

    @Override
    public BlockDestructionPacket fromNMSBlockDestructionPacket(ClientboundBlockDestructionPacket from) {
        return new BlockDestructionPacket(from.getId(), asBlockVector(from.getPos()), from.getProgress());
    }

    @Override
    public Class<ClientboundBlockDestructionPacket> getNMSBlockDestructionPacketClass() {
        return ClientboundBlockDestructionPacket.class;
    }
}
