package it.jakegblp.nms.api;

import com.google.common.collect.BiMap;
import net.kyori.adventure.platform.bukkit.MinecraftComponentSerializer;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.entity.Pose;
import org.bukkit.util.BlockVector;
import org.bukkit.util.Vector;

import static it.jakegblp.nms.api.utils.ReflectionUtils.*;

public interface ConversionAdapter<
        NMSVector,
        NMSBlockPos,
        NMSServerPlayer,
        NMSPose,
        NMSComponent,
        NMSPacket
        > {
    String CRAFT_BUKKIT_PACKAGE = Bukkit.getServer().getClass().getPackage().getName();
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
        return (Class<? extends Player>) forClassName(CRAFT_BUKKIT_PACKAGE+".entity.CraftPlayer");
    }
    @SuppressWarnings({"UnstableApiUsage", "unchecked"})
    default NMSComponent asNMSComponent(Component component) {
        return (NMSComponent) MinecraftComponentSerializer.get().serialize(component);
    }
    Class<NMSComponent> getNMSComponentClass();

    @SuppressWarnings("UnstableApiUsage")
    default Component asComponent(NMSComponent nmsComponent) {
        return MinecraftComponentSerializer.get().deserialize(nmsComponent);
    }
    void sendPacket(NMSServerPlayer player, NMSPacket packet);

}
