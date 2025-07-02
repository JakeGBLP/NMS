package it.jakegblp.nms;

import com.github.zafarkhaja.semver.Version;
import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIBukkitConfig;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.ArgumentSuggestions;
import dev.jorel.commandapi.arguments.StringArgument;
import it.jakegblp.nms.api.NMSApi;
import it.jakegblp.nms.api.entity.metadata.EntityMetadata;
import it.jakegblp.nms.api.packets.EntityMetadataPacket;
import it.jakegblp.nms.api.packets.EntitySpawnPacket;
import it.jakegblp.nms.impl.*;
import lombok.Getter;
import lombok.NonNull;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import java.util.Arrays;
import java.util.Random;
import java.util.UUID;

import static com.github.zafarkhaja.semver.Version.of;
import static it.jakegblp.nms.api.NMSAdapter.nmsAdapter;

@Getter
public class NMS extends JavaPlugin {

    @Getter
    private static NMS instance;
    public static final Random RANDOM = new Random();

    @Override
    public void onLoad() {
        CommandAPI.onLoad(new CommandAPIBukkitConfig(this)
                .shouldHookPaperReload(true)
                .usePluginNamespace());
    }

    @Override
    public void onEnable() {
        CommandAPI.onEnable();
        instance = this;
        Version version = Version.parse(Bukkit.getBukkitVersion()).toStableVersion();
        int
                major = (int) version.majorVersion(),
                minor = (int) version.minorVersion(),
                patch = (int) version.patchVersion();
        /*
         Fun Fact:
         The major version probably doesn't need a switch statement as Mojang will
         likely never make Minecraft 2.0, I'm keeping it for consistency with the minor and patch versions.
         */
        if (major == 1) {
            nmsAdapter = switch (minor) {
                case 17 -> switch (patch) {
                    case 0 -> new v1_17();
                    case 1 -> new v1_17_1();
                    default -> null;
                };
                case 18 -> switch (patch) {
                    case 0 -> new v1_18();
                    case 1 -> new v1_18_1();
                    case 2 -> new v1_18_2();
                    default -> null;
                };
                case 19 -> switch (patch) {
                    case 0 -> new v1_19();
                    case 1, 2 -> new v1_19_1();
                    case 3 -> new v1_19_3();
                    case 4 -> new v1_19_4();
                    default -> null;
                };
                case 20 -> switch (patch) {
                    case 0, 1 -> new v1_20_1();
                    case 2 -> new v1_20_2();
                    case 3, 4 -> new v1_20_4();
                    case 5, 6 -> new v1_20_6();
                    default -> null;
                };
                case 21 -> switch (patch) {
                    case 0, 1 -> new v1_21_1();
                    case 2, 3 -> new v1_21_3();
                    case 4 -> new v1_21_4();
                    default -> null;
                };
                default -> null;
            };
            if (nmsAdapter != null) {
                if (version.isHigherThanOrEquivalentTo(of(1, 19, 3))) {
                    From_1_19_3 from_1_19_3To1211 = new From_1_19_3();
                    nmsAdapter.entityTypeAdapter = from_1_19_3To1211;
                    nmsAdapter.entityMetadataPacketAdapter = from_1_19_3To1211;
                } else {
                    To_1_19_1 from_1_19_1 = new To_1_19_1();
                    nmsAdapter.entityTypeAdapter = from_1_19_1;
                    nmsAdapter.entityMetadataPacketAdapter = from_1_19_1;
                }
                if (version.isHigherThanOrEquivalentTo(of(1, 19)))
                    nmsAdapter.entitySpawnPacketAdapter = new From_1_19();
                else
                    nmsAdapter.entitySpawnPacketAdapter = new To_1_18_2();
            }
        } else {
            nmsAdapter = null;
        }

        if (nmsAdapter == null)
            throw new IllegalStateException("NMS implementation not found");
        nmsAdapter.adventure = BukkitAudiences.create(this);

        new CommandAPICommand("nms")
                .withArguments(
                        new StringArgument("packet").replaceSuggestions(
                                ArgumentSuggestions.strings("spawn-entity", "entity-metadata")))
                .executesPlayer((player, args) -> {
                    getLogger().info(Arrays.toString(args.args()));
                    if (args.get("packet") instanceof String s && s.equals("spawn-entity")) {
                        player.sendMessage("Spawned fake iron golem at your location.");
                        getLogger().info("Spawned fake iron golem at "+player+"'s location.");
                        Location location = player.getLocation();
                        NMSApi.sendPacket(player, new EntitySpawnPacket(
                                NMSApi.generateRandomId(),
                                UUID.randomUUID(),
                                location.getX(),
                                location.getY(),
                                location.getZ(),
                                location.getPitch(),
                                location.getYaw(),
                                EntityType.ZOMBIE,
                                0,
                                new Vector()
                        ));
                    } else if (args.get("packet") instanceof String s && s.equals("entity-metadata")) {
                        int id = NMSApi.getRandomID();
                        player.sendMessage("Changed entity metadata of entity with id "+id);
                        getLogger().info("Changed entity metadata of entity with id "+id);
                        EntityMetadata EntityMetadata = new EntityMetadata();
                        EntityMetadata.EntityFlags entityFlags = new EntityMetadata.EntityFlags();
                        entityFlags.setGlowing(true);
                        entityFlags.setOnFire(true);
                        EntityMetadata.setEntityFlags(entityFlags);
                        NMSApi.sendPacket(player, new EntityMetadataPacket<>(id, Entity.class, EntityMetadata));
                    }
                })
                .register();

        getLogger().info("NMS has been enabled!");
        getLogger().info("- Server version: " + version);
    }

    @Override
    public void onDisable() {
        CommandAPI.onDisable();
        nmsAdapter.adventure.close();
        nmsAdapter.adventure = null;
        nmsAdapter = null;
        getLogger().info("NMS has been disabled!");
    }

    public @NonNull BukkitAudiences adventure() {
        if(nmsAdapter.adventure == null) {
            throw new IllegalStateException("Tried to access Adventure when the plugin was disabled!");
        }
        return nmsAdapter.adventure;
    }

}
