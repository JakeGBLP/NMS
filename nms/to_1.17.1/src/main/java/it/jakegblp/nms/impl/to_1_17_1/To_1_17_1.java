package it.jakegblp.nms.impl.to_1_17_1;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import io.netty.buffer.Unpooled;
import it.jakegblp.nms.api.*;
import it.jakegblp.nms.api.entity.metadata.EntitySerializerInfo;
import it.jakegblp.nms.api.entity.metadata.key.MetadataKey;
import it.jakegblp.nms.api.packets.EntityMetadataPacket;
import it.jakegblp.nms.api.packets.EntitySpawnPacket;
import lombok.Getter;
import net.kyori.adventure.platform.bukkit.MinecraftComponentSerializer;
import net.kyori.adventure.text.Component;
import net.minecraft.core.BlockPosition;
import net.minecraft.core.IRegistry;
import net.minecraft.network.PacketDataSerializer;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.PacketPlayOutEntityMetadata;
import net.minecraft.network.protocol.game.PacketPlayOutSpawnEntity;
import net.minecraft.network.syncher.DataWatcher;
import net.minecraft.network.syncher.DataWatcherSerializer;
import net.minecraft.resources.MinecraftKey;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.world.entity.EntityPose;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.phys.Vec3D;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.v1_17_R1.util.CraftVector;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Pose;
import org.bukkit.util.BlockVector;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static it.jakegblp.nms.api.NMSAdapter.NMS;
import static it.jakegblp.nms.api.entity.metadata.EntitySerializerInfo.Type.OPTIONAL;

@Getter
public class To_1_17_1 implements
        EntitySpawnPacketAdapter<PacketPlayOutSpawnEntity>, EntityTypeAdapter<EntityTypes<?>>,
        EntityMetadataPacketAdapter<PacketPlayOutEntityMetadata>,
        ResourceLocationAdapter<MinecraftKey>,
        ConversionAdapter<
                Vec3D,
                BlockPosition,
                EntityPlayer,
                EntityPose,
                IChatBaseComponent,
                Packet<?>
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
    @SuppressWarnings({"UnstableApiUsage"})
    public IChatBaseComponent asNMSComponent(Component component) {
        return (IChatBaseComponent) MinecraftComponentSerializer.get().serialize(component);
    }

    @Override
    public Class<IChatBaseComponent> getNMSComponentClass() {
        return IChatBaseComponent.class;
    }

    @Override
    @SuppressWarnings("unchecked")
    public PacketPlayOutEntityMetadata toNMSEntityMetadataPacket(EntityMetadataPacket<?, ?> from) {
        PacketDataSerializer serializer = new PacketDataSerializer(Unpooled.buffer());
        serializer.d(from.getEntityId());
        List<DataWatcher.Item<?>> items = new ArrayList<>();
        for (Map.Entry<MetadataKey<? extends Entity, ?>, Object> metadataKeyObjectEntry : from.getEntityMetadata()
                .getMetadataItems()
                .entrySet()) {
            if (metadataKeyObjectEntry != null) {
                MetadataKey<?, ?> key = metadataKeyObjectEntry.getKey();
                Object nmsValue = NMS.asNMSObject(metadataKeyObjectEntry.getValue());
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
    public EntityTypes<?> toNMSEntityType(EntityType from) {
        return IRegistry.Y.get(asResourceLocation(from.getKey()));
    }

    @Override
    public EntityType toEntityType(EntityTypes<?> to) {
        return org.bukkit.Registry.ENTITY_TYPE.get(asNamespacedKey(IRegistry.Y.getKey(to)));
    }

    @Override
    public MinecraftKey asResourceLocation(NamespacedKey namespacedKey) {
        return new MinecraftKey(namespacedKey.getNamespace(), namespacedKey.getKey());
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
    public void sendPacket(EntityPlayer entityPlayer, Packet<?> packet) {
        entityPlayer.b.sendPacket(packet);
    }
}
