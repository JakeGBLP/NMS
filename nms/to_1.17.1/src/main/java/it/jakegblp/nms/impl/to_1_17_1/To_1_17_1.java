package it.jakegblp.nms.impl.to_1_17_1;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import com.mojang.datafixers.util.Pair;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import it.jakegblp.nms.api.NMSApi;
import it.jakegblp.nms.api.adapters.*;
import it.jakegblp.nms.api.entity.metadata.EntitySerializerInfo;
import it.jakegblp.nms.api.entity.metadata.MetadataKey;
import it.jakegblp.nms.api.packets.client.BlockDestructionPacket;
import it.jakegblp.nms.api.packets.client.EntityMetadataPacket;
import it.jakegblp.nms.api.packets.client.EntitySpawnPacket;
import it.jakegblp.nms.api.packets.client.SetEquipmentPacket;
import lombok.Getter;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.minecraft.core.BlockPosition;
import net.minecraft.core.IRegistry;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.PacketDataSerializer;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.PacketPlayOutBlockBreakAnimation;
import net.minecraft.network.protocol.game.PacketPlayOutEntityEquipment;
import net.minecraft.network.protocol.game.PacketPlayOutEntityMetadata;
import net.minecraft.network.protocol.game.PacketPlayOutSpawnEntity;
import net.minecraft.network.syncher.DataWatcher;
import net.minecraft.network.syncher.DataWatcherSerializer;
import net.minecraft.resources.MinecraftKey;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.server.network.PlayerConnection;
import net.minecraft.world.entity.EntityPose;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.EnumItemSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3D;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.v1_17_R1.util.CraftVector;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Pose;
import org.bukkit.util.BlockVector;
import org.bukkit.util.Vector;

import java.util.*;

import static it.jakegblp.nms.api.AbstractNMS.NMS;
import static it.jakegblp.nms.api.entity.metadata.EntitySerializerInfo.Type.OPTIONAL;
import static it.jakegblp.nms.api.utils.StructureTranslation.fromMapToPairList;
import static it.jakegblp.nms.api.utils.StructureTranslation.fromPairListToMap;

@Getter
public class To_1_17_1 implements
        SetEquipmentPacketAdapter<PacketPlayOutEntityEquipment>,
        EntitySpawnPacketAdapter<PacketPlayOutSpawnEntity>,
        EntityTypeAdapter<EntityTypes>,
        EntityMetadataPacketAdapter<PacketPlayOutEntityMetadata>,
        ResourceLocationAdapter<MinecraftKey>,
        MajorChangesAdapter<
                EnumItemSlot,
                ItemStack,
                Vec3D,
                BlockPosition,
                EntityPlayer,
                EntityPose,
                IChatBaseComponent,
                Packet,
                PlayerConnection,
                NetworkManager,
                PacketPlayOutBlockBreakAnimation
                > {

    private final BiMap<Pose, EntityPose> poseMap;

    public To_1_17_1() {
        poseMap = ImmutableBiMap.of(org.bukkit.entity.Pose.SNEAKING, EntityPose.f);
    }

    @Override
    public PacketPlayOutSpawnEntity toNMSEntitySpawnPacket(EntitySpawnPacket from) {
        return new PacketPlayOutSpawnEntity(
                from.getEntityId(),
                from.getEntityUUID(),
                from.getX(),
                from.getY(),
                from.getZ(),
                from.getPitch(),
                from.getYaw(),
                (EntityTypes<?>) NMS.toNMSEntityType(from.getEntityType()),
                from.getData(),
                asNMSVector(from.getVelocity()));
    }

    @Override
    public EntitySpawnPacket fromNMSEntitySpawnPacket(PacketPlayOutSpawnEntity from) {
        return new EntitySpawnPacket(
                from.b(),
                from.c(),
                from.d(),
                from.e(),
                from.f(),
                from.j(),
                from.k(),
                fromNMSEntityType(from.l()),
                from.m(),
                new Vector(from.g(), from.h(), from.i())
        );
    }

    @Override
    public Class<PacketPlayOutSpawnEntity> getNMSEntitySpawnPacketClass() {
        return PacketPlayOutSpawnEntity.class;
    }

    @Override
    public Class<IChatBaseComponent> getNMSComponentClass() {
        return IChatBaseComponent.class;
    }

    @Override
    public void sendPacketInternal(PlayerConnection playerConnection, Packet packet) {
        playerConnection.sendPacket(packet);
    }

    @Override
    public NetworkManager getConnection(PlayerConnection playerConnection) {
        return playerConnection.a;
    }

    @Override
    public Channel getChannel(NetworkManager networkManager) {
        return networkManager.k;
    }

    @Override
    @SuppressWarnings("unchecked")
    public PacketPlayOutEntityMetadata toNMSEntityMetadataPacket(EntityMetadataPacket from) {
        PacketDataSerializer serializer = new PacketDataSerializer(Unpooled.buffer());
        serializer.d(from.getEntityId());
        List<DataWatcher.Item<?>> items = new ArrayList<>();
        for (Map.Entry<? extends MetadataKey<? extends Entity, ?>, Object> metadataKeyObjectEntry : from
                .getEntityMetadata()
                .metadataItems()
                .entrySet()) {
            if (metadataKeyObjectEntry != null) {
                MetadataKey<?, ?> key = metadataKeyObjectEntry.getKey();
                Object nmsValue = NMS.toNMSObject(metadataKeyObjectEntry.getValue());
                EntitySerializerInfo.Type type = key.getEntitySerializerInfo().serializerType();
                items.add(new DataWatcher.Item<>(
                        ((DataWatcherSerializer<Object>)
                                NMS.getEntityDataSerializer(NMS.getSerializableClass(key.getObjectClass()), type))
                                .a(key.getIndex()),
                        type == OPTIONAL ? Optional.ofNullable(nmsValue) : nmsValue
                ));
            }
        }
        DataWatcher.a(items, serializer);
        return new PacketPlayOutEntityMetadata(serializer);
    }

    @Override
    public EntityMetadataPacket fromNMSEntityMetadataPacket(PacketPlayOutEntityMetadata from) {
        List<DataWatcher.Item<?>> items = from.b();
        if (items == null) return null;
        Map<MetadataKey<? extends org.bukkit.entity.Entity, ?>, Object> metadata = new HashMap<>();
        for (DataWatcher.Item<?> item : items) {
            Object value = item.b();
            metadata.put(new MetadataKey<>(org.bukkit.entity.Entity.class, item.a().a(), value), value);
        }
        return new EntityMetadataPacket(from.c(), metadata);
    }

    @Override
    public Class<PacketPlayOutEntityMetadata> getNMSEntityMetadataPacketClass() {
        return PacketPlayOutEntityMetadata.class;
    }

    @Override
    public EntityTypes<?> toNMSEntityType(EntityType from) {
        return IRegistry.Y.get(toNMSNamespacedKey(from.getKey()));
    }

    @Override
    public EntityType fromNMSEntityType(EntityTypes to) {
        return org.bukkit.Registry.ENTITY_TYPE.get(fromNMSNamespacedKey(IRegistry.Y.getKey(to)));
    }

    @Override
    public Class<EntityTypes> getNMSEntityTypeClass() {
        return EntityTypes.class;
    }

    @Override
    public MinecraftKey toNMSNamespacedKey(NamespacedKey namespacedKey) {
        return new MinecraftKey(namespacedKey.getNamespace(), namespacedKey.getKey());
    }

    @Override
    public Class<MinecraftKey> getNMSNamespacedKeyClass() {
        return MinecraftKey.class;
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
    public Class<EnumItemSlot> getNMSEquipmentSlotClass() {
        return EnumItemSlot.class;
    }

    @Override
    public Vector asVector(Vec3D vec3D) {
        // Using craft classes usually errors when using different versions, here it's fine, but not future-proofed.
        return CraftVector.toBukkit(vec3D);
    }

    @Override
    public Vec3D asNMSVector(Vector vector) {
        // Using craft classes usually errors when using different versions, here it's fine, but not future-proofed.
        return CraftVector.toNMS(vector);
    }

    @Override
    public Class<Vec3D> getNMSVectorClass() {
        return Vec3D.class;
    }

    @Override
    public BlockVector asBlockVector(BlockPosition blockPosition) {
        return new BlockVector(blockPosition.getX(), blockPosition.getY(), blockPosition.getZ());
    }

    @Override
    public BlockPosition asNMSBlockVector(BlockVector blockVector) {
        return new BlockPosition(blockVector.getBlockX(), blockVector.getBlockY(), blockVector.getBlockZ());
    }

    @Override
    public Class<BlockPosition> getNMSBlockVectorClass() {
        return BlockPosition.class;
    }

    @Override
    public Pose asPose(EntityPose entityPose) {
        return getPoseMap().inverse().getOrDefault(entityPose, org.bukkit.entity.Pose.valueOf(entityPose.toString()));
    }

    @Override
    public EntityPose asNMSPose(Pose pose) {
        return getPoseMap().getOrDefault(pose, EntityPose.valueOf(pose.toString()));
    }

    @Override
    public Class<EntityPose> getNMSPoseClass() {
        return EntityPose.class;
    }

    @Override
    public Player asPlayer(EntityPlayer entityPlayer) {
        return entityPlayer.getBukkitEntity();
    }

    @Override
    public Class<EntityPlayer> getNMSServerPlayerClass() {
        return EntityPlayer.class;
    }

    @Override
    public PlayerConnection getPlayerConnection(EntityPlayer entityPlayer) {
        return entityPlayer.b;
    }

    @Override
    public IChatBaseComponent asNMSComponent(net.kyori.adventure.text.Component component) {
        return IChatBaseComponent.ChatSerializer.a(GsonComponentSerializer.gson().serialize(component));
    }

    @Override
    public net.kyori.adventure.text.Component asComponent(IChatBaseComponent component) {
        return GsonComponentSerializer.gson().deserialize(IChatBaseComponent.ChatSerializer.a(component));
    }

    @Override
    public PacketPlayOutBlockBreakAnimation toNMSBlockDestructionPacket(BlockDestructionPacket from) {
        return new PacketPlayOutBlockBreakAnimation(from.getEntityId(), asNMSBlockVector(from.getPosition()), from.getBlockDestructionStage());
    }

    @Override
    public BlockDestructionPacket fromNMSBlockDestructionPacket(PacketPlayOutBlockBreakAnimation from) {
        return new BlockDestructionPacket(from.b(), asBlockVector(from.c()), from.d());
    }

    @Override
    public Class<PacketPlayOutBlockBreakAnimation> getNMSBlockDestructionPacketClass() {
        return PacketPlayOutBlockBreakAnimation.class;
    }

    @Override
    @SuppressWarnings("unchecked")
    public PacketPlayOutEntityEquipment toNMSSetEquipmentPacket(SetEquipmentPacket from) {
        Object list = fromMapToPairList(from.getEquipment(), NMSApi::asNMSEquipmentSlot, NMSApi::asNMSItemStack);
        return new PacketPlayOutEntityEquipment(from.getEntityId(), (List<Pair<EnumItemSlot, ItemStack>>) list);
    }

    @Override
    public SetEquipmentPacket fromNMSSetEquipmentPacket(PacketPlayOutEntityEquipment from) {
        return new SetEquipmentPacket(from.b(), fromPairListToMap(from.c(), NMSApi::asEquipmentSlot, NMSApi::asItemStack));
    }

    @Override
    public Class<PacketPlayOutEntityEquipment> getNMSSetEquipmentPacketClass() {
        return PacketPlayOutEntityEquipment.class;
    }
}
