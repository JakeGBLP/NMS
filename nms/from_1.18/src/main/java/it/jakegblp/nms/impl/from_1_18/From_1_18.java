package it.jakegblp.nms.impl.from_1_18;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import it.jakegblp.nms.api.ConversionAdapter;
import lombok.Getter;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.phys.Vec3;
import org.bukkit.entity.Player;
import org.bukkit.util.BlockVector;
import org.bukkit.util.Vector;

@Getter
public class From_1_18 implements
        ConversionAdapter<
                Vec3,
                BlockPos,
                ServerPlayer,
                Pose,
                Component,
                Packet<?>
                > {

    private final BiMap<org.bukkit.entity.Pose, Pose> poseMap;

    public From_1_18() {
        poseMap = ImmutableBiMap.of(org.bukkit.entity.Pose.SNEAKING, Pose.CROUCHING);
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
    public Class<Component> getNMSComponentClass() {
        return Component.class;
    }

    @Override
    public void sendPacket(ServerPlayer serverPlayer, Packet<?> packet) {
        serverPlayer.connection.send(packet);
    }
}
