package it.jakegblp.nms;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

import static it.jakegblp.nms.utils.ReflectionUtils.*;

@Getter
public abstract class NMSAdapter<
        NMSNameable,
        NMSEntity extends NMSNameable,
        NMSLivingEntity extends NMSEntity,
        NMSPlayer extends NMSLivingEntity,
        NMSServerPlayer extends NMSPlayer,
        Packet,
        ClientboundAddEntityPacket
        > {

    public final Version MINECRAFT_VERSION = Version.parse(Bukkit.getBukkitVersion().split("-")[0]);
    public final ObfuscationMap obfuscationMap = forVersion(MINECRAFT_VERSION.toString());
    public final String CRAFTBUKKIT_PACKAGE = Bukkit.getServer().getClass().getPackage().getName();
    @NotNull
    @SuppressWarnings("ConstantConditions")
    public final Class<?> CRAFT_PLAYER_CLASS = forName(CRAFTBUKKIT_PACKAGE + ".entity.CraftPlayer");
    @NotNull
    @SuppressWarnings("ConstantConditions")
    public final Class<?> SERVER_PLAYER_CLASS = forName("net.minecraft.server.level.EntityPlayer");
    public final Class<?> SERVER_GAME_PACKET_LISTENER_IMPL_CLASS = forName("net.minecraft.server.network.PlayerConnection");
    @SuppressWarnings("ConstantConditions")
    public final Method CRAFT_PLAYER_GET_HANDLE_METHOD = getMethod(CRAFT_PLAYER_CLASS, "getHandle");
    @SuppressWarnings("ConstantConditions")
    public final Method SERVER_GAME_PACKET_LISTENER_IMPL_SEND_PACKET_METHOD = getDeclaredMethod(SERVER_GAME_PACKET_LISTENER_IMPL_CLASS, "b");
    public final Field SERVER_PLAYER_CONNECTION_FIELD = getField(CRAFT_PLAYER_CLASS, "connection");

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
    public NMSServerPlayer getServerPlayer(Player player) {
        return (NMSServerPlayer) invoke(CRAFT_PLAYER_GET_HANDLE_METHOD, CRAFT_PLAYER_CLASS.cast(player));
    }

    @Getter
    public abstract class GenericNMSServerPlayer {

        private final NMSServerPlayer serverPlayer;

        public GenericNMSServerPlayer(Player player) {
            serverPlayer = NMSAdapter.this.getServerPlayer(player);
        }

        @SuppressWarnings("ConstantConditions")
        public void sendPacket(Packet packet) {
            invoke(SERVER_GAME_PACKET_LISTENER_IMPL_SEND_PACKET_METHOD, getFieldValue(SERVER_PLAYER_CONNECTION_FIELD, getServerPlayer()), packet);
        }
    }
}

