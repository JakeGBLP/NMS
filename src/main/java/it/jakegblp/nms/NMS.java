package it.jakegblp.nms;

import com.github.zafarkhaja.semver.Version;
import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIBukkitConfig;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.*;
import it.jakegblp.nms.api.NMSApi;
import it.jakegblp.nms.api.NMSFactory;
import it.jakegblp.nms.api.entity.metadata.EntityFlags;
import it.jakegblp.nms.api.entity.metadata.HandStates;
import it.jakegblp.nms.api.entity.metadata.LivingEntityMetadata;
import it.jakegblp.nms.api.packets.EntityMetadataPacket;
import it.jakegblp.nms.api.packets.EntitySpawnPacket;
import it.jakegblp.nms.api.packets.PlayerRotationPacket;
import lombok.Getter;
import lombok.NonNull;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import java.util.Random;
import java.util.UUID;

import static it.jakegblp.nms.api.NMSAdapter.NMS;

@Getter
public class NMS extends JavaPlugin {

    private static int randomID;

    public static NMS instance;
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
        NMS = NMSFactory.createNMS(this, version);

        new CommandAPICommand("nms")
                .withPermission("nms.command.packet")
                .withArguments(
                        new StringArgument("packet").replaceSuggestions(
                                ArgumentSuggestions.strings("spawn-entity", "entity-metadata", "player-rotation")),
                        new FloatArgument("pitch"), new FloatArgument("yaw"))
                .withSubcommand(new CommandAPICommand("spawn-entity")
                        .withPermission("nms.command.packet.spawn_entity")
                        .withArguments(new EntityTypeArgument("entity-type"))
                        .executesPlayer((player, args) -> {
                            EntityType entityType = (EntityType) args.get(0);
                            player.sendMessage("Spawned fake "+entityType+" at your location.");
                            getLogger().info("Spawned fake "+entityType+" at "+player.getName()+"'s location.");
                            Location location = player.getLocation();
                            NMSApi.sendPacket(player, new EntitySpawnPacket(
                                    generateRandomId(),
                                    UUID.randomUUID(),
                                    location.getX(),
                                    location.getY(),
                                    location.getZ(),
                                    location.getPitch(),
                                    location.getYaw(),
                                    entityType,
                                    0,
                                    new Vector()
                            ));
                        }))
                .withSubcommand(new CommandAPICommand("entity-metadata")
                        .withPermission("nms.command.packet.entity_metadata")
                        .withArguments(new GreedyStringArgument("name-in-minimessage").setOptional(true))
                        .executesPlayer((player, args) -> {
                            Component name;
                            if (args.get("name-in-minimessage") instanceof String string) {
                                name = MiniMessage.miniMessage().deserialize(string);
                            } else {
                                name = Component.text("Hello").color(NamedTextColor.RED);
                            }
                            int id = getLastRandomID();
                            player.sendMessage("Changed entity metadata of entity with id "+id);
                            getLogger().info("Changed entity metadata of entity with id "+id);
                            LivingEntityMetadata metadata = new LivingEntityMetadata();
                            metadata.setHandStates(new HandStates().setRiptiding(true));
                            metadata.setEntityFlags(new EntityFlags().setGlowing(true).setOnFire(true));
                            metadata.setCustomName(name);
                            metadata.setCustomNameVisible(true);
                            NMSApi.sendPacket(player, new EntityMetadataPacket<>(id, LivingEntity.class, metadata));
                        }))
                .withSubcommand(new CommandAPICommand("player-rotation")
                        .withPermission("nms.command.packet.player_rotation")
                        .withArguments(new FloatArgument("pitch"), new FloatArgument("yaw"))
                        .executesPlayer((player, args) -> {
                            Float pitch = (Float) args.get(0),
                                    yaw = (Float) args.get(1);
                            player.sendMessage("Rotated "+player.getName()+" by pitch "+pitch+" and yaw "+yaw);
                            getLogger().info("Rotated "+player.getName()+" by pitch "+pitch+" and yaw "+yaw);
                            NMSApi.sendPacket(player, new PlayerRotationPacket(pitch, yaw));
                        }))
                .register();

        getLogger().info("NMS has been enabled!");
        getLogger().info("- Server version: " + version);
    }

    @Override
    public void onDisable() {
        CommandAPI.onDisable();
        NMS.disable();
        NMS = null;
        getLogger().info("NMS has been disabled!");
    }

    public @NonNull BukkitAudiences adventure() {
        if (NMS.adventure == null) {
            throw new IllegalStateException("Tried to access Adventure when the plugin was disabled!");
        }
        return NMS.adventure;
    }

    public int generateRandomId() {
        randomID = RANDOM.nextInt();
        return randomID;
    }

    public static int getLastRandomID() {
        return randomID;
    }

}
