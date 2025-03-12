package it.jakegblp.nms;

import it.jakegblp.nms.packets.EntitySpawnPacket;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Map;

import static it.jakegblp.nms.utils.CraftBukkitUtils.getCraftBukkitClass;
import static it.jakegblp.nms.utils.ReflectionUtils.*;

@Getter
public abstract class NMSAdapter<
        NMSNameable,
        NMSEntity extends NMSNameable,
        NMSLivingEntity extends NMSEntity,
        NMSPlayer extends NMSLivingEntity,
        NMSServerPlayer extends NMSPlayer,
        NMSPacket,
        NMSEntitySpawnPacket extends NMSPacket
        > {

    public final Version MINECRAFT_VERSION = Version.parse(Bukkit.getBukkitVersion().split("-")[0]);
    public final ObfuscationMap obfuscationMap = forVersion(MINECRAFT_VERSION.toString());

    public record ObfuscationMap(
            String playerPacketListenerFieldName,
            String sendPacketMethodName) { }

    private static final Map<String, ObfuscationMap> VERSION_MAPPINGS = Map.ofEntries(
            Map.entry("1.21.4", new ObfuscationMap("f", "b")),
            Map.entry("1.21.3", new ObfuscationMap("f", "b")),
            Map.entry("1.21.1", new ObfuscationMap("c", "b")),
            Map.entry("1.20.6", new ObfuscationMap("c", "b")),
            Map.entry("1.20.4", new ObfuscationMap("c", "b")),
            Map.entry("1.20.2", new ObfuscationMap("c", "b")),
            Map.entry("1.20.1", new ObfuscationMap("c", "a")),
            Map.entry("1.19.4", new ObfuscationMap("b", "a")),
            Map.entry("1.19.3", new ObfuscationMap("b", "a")),
            Map.entry("1.19.1", new ObfuscationMap("b", "a")),
            Map.entry("1.19",   new ObfuscationMap("b", "a")),
            Map.entry("1.18.2", new ObfuscationMap("b", "a")),
            Map.entry("1.18.1", new ObfuscationMap("b", "a")),
            Map.entry("1.18",   new ObfuscationMap("b", "a")),
            Map.entry("1.17.1", new ObfuscationMap("b", "a")),
            Map.entry("1.17",   new ObfuscationMap("b", "a"))
    );

    public static ObfuscationMap forVersion(String version) {
        return VERSION_MAPPINGS.getOrDefault(version, VERSION_MAPPINGS.get("1.21.4"));
    }

    @SuppressWarnings({"unchecked", "ConstantConditions"})
    public NMSServerPlayer asNMSServerPlayer(Player player) {
        Class<?> craftBukkitCraftPlayerClass = getCraftBukkitClass("entity.CraftPlayer");
        return (NMSServerPlayer) invokeSafely(getMethodSafely(craftBukkitCraftPlayerClass, "getHandle"),
                craftBukkitCraftPlayerClass.cast(player));
    }

    public abstract NMSEntitySpawnPacket asNMSEntitySpawnPacket(EntitySpawnPacket packet);

    @Getter
    public abstract class GenericNMSServerPlayer {

        private final NMSServerPlayer serverPlayer;

        public GenericNMSServerPlayer(Player player) {
            serverPlayer = NMSAdapter.this.asNMSServerPlayer(player);
        }

        public GenericNMSServerPlayer(NMSServerPlayer player) {
            serverPlayer = player;
        }

        @SuppressWarnings("ConstantConditions")
        public void sendPacket(NMSPacket packet) {
            invokeSafely(getCachedDeclaredMethod(
                    getCachedClass("net.minecraft.server.network.PlayerConnection"),
                    obfuscationMap.playerPacketListenerFieldName),
                    getFieldValueSafely(getCachedField(
                            getCraftBukkitClass("entity.CraftPlayer"),
                            "connection"), getServerPlayer()),
                    packet);
        }

        public void sendEntitySpawnPacket(EntitySpawnPacket packet) {
            sendPacket(asNMSEntitySpawnPacket(packet));
        }
    }
}

