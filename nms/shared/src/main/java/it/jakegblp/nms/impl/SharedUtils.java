package it.jakegblp.nms.impl;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import it.jakegblp.nms.api.NMSObject;
import net.kyori.adventure.platform.bukkit.MinecraftComponentSerializer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.phys.Vec3;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.v1_21_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.util.BlockVector;
import org.bukkit.util.Vector;

public class SharedUtils {

    public static BiMap<org.bukkit.entity.Pose, Pose> POSE_MAP = ImmutableBiMap.of(org.bukkit.entity.Pose.SNEAKING, Pose.CROUCHING);

    public static ServerPlayer asServerPlayer(Player player) {
        return ((CraftPlayer) player).getHandle();
    }

    public static Player asPlayer(ServerPlayer serverPlayer) {
        return serverPlayer.getBukkitEntity();
    }

    public static Player asPlayer(Object serverPlayer) {
        return asPlayer((ServerPlayer)serverPlayer);
    }

    public static Pose asNMSPose(org.bukkit.entity.Pose pose) {
        return POSE_MAP.getOrDefault(pose, Pose.valueOf(pose.name()));
    }

    public static org.bukkit.entity.Pose asPose(Pose nmsPose) {
        return POSE_MAP.inverse().getOrDefault(nmsPose, org.bukkit.entity.Pose.valueOf(nmsPose.name()));
    }

    public static BlockPos asNMSBlockPosition(BlockVector blockVector) {
        return new BlockPos(blockVector.getBlockX(), blockVector.getBlockY(), blockVector.getBlockZ());
    }

    public static BlockVector asBlockVector(Vec3i baseBlockPosition) {
        return new BlockVector(baseBlockPosition.getX(), baseBlockPosition.getY(), baseBlockPosition.getZ());
    }

    public static Vec3 asNMSVector(Vector vector) {
        return new Vec3(vector.getX(), vector.getY(), vector.getZ());
    }

    public static Vector asVector(Vec3 vec3) {
        return new Vector(vec3.x, vec3.y, vec3.z);
    }

    @SuppressWarnings({"UnstableApiUsage"})
    public static Component asNMSComponent(net.kyori.adventure.text.Component component) {
        return (Component) MinecraftComponentSerializer.get().serialize(component);
    }

    @SuppressWarnings({"UnstableApiUsage"})
    public static net.kyori.adventure.text.Component asComponent(Component nmsComponent) {
        return MinecraftComponentSerializer.get().deserialize(nmsComponent);
    }

    public static ResourceLocation asResourceLocation(NamespacedKey namespacedKey) {
        return ResourceLocation.fromNamespaceAndPath(namespacedKey.getNamespace(), namespacedKey.getKey());
    }

    public static NamespacedKey asNamespacedKey(ResourceLocation resourceLocation) {
        return NamespacedKey.fromString(resourceLocation.toString());
    }

    public static void sendPacket(ServerPlayer player, Object packet) {
        player.connection.sendPacket((Packet<?>) packet);
    }

    public static void sendPacket(Player player, Object packet) {
        asServerPlayer(player).connection.sendPacket((Packet<?>) packet);
    }

    public static Object asNMS(Object object) {
        if (object instanceof NMSObject<?> nmsObject) {
            return nmsObject.asNMS();
        } else if (object instanceof net.kyori.adventure.text.Component component) {
            return asNMSComponent(component);
        } else if (object instanceof org.bukkit.entity.Pose pose) {
            return asNMSPose(pose);
        } else if (object instanceof BlockVector blockVector) {
            return asNMSBlockPosition(blockVector);
        } else if (object instanceof Player player) {
            return asServerPlayer(player);
        }
        return object;
    }
}
