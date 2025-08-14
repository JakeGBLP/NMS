package it.jakegblp.nms;

import ch.njol.skript.Skript;
import ch.njol.skript.SkriptAddon;
import com.github.zafarkhaja.semver.Version;
import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIBukkitConfig;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.*;
import it.jakegblp.nms.api.NMSApi;
import it.jakegblp.nms.api.NMSFactory;
import it.jakegblp.nms.api.entity.metadata.EntityMetadata;
import it.jakegblp.nms.api.entity.metadata.MetadataKeyRegistries;
import it.jakegblp.nms.api.entity.metadata.wrappers.EntityFlags;
import it.jakegblp.nms.api.entity.metadata.wrappers.HandStates;
import it.jakegblp.nms.api.packets.EntityMetadataPacket;
import it.jakegblp.nms.api.packets.EntitySpawnPacket;
import it.jakegblp.nms.api.packets.PlayerRotationPacket;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import java.io.IOException;
import java.util.UUID;

import static it.jakegblp.nms.api.AbstractNMS.NMS;
import static it.jakegblp.nms.api.NMSApi.generateRandomId;
import static it.jakegblp.nms.api.NMSApi.getLastRandomID;

@Getter
public class NMS extends JavaPlugin {
    public static NMS instance;

    private SkriptAddon addon;

    public static boolean isPluginEnabled(String pluginName) {
        Plugin plugin = Bukkit.getPluginManager().getPlugin(pluginName);
        return plugin != null && plugin.isEnabled();
    }

    @Override
    public void onLoad() {
        CommandAPI.onLoad(new CommandAPIBukkitConfig(this)
                .shouldHookPaperReload(true));
    }

    @Override
    public void onEnable() {
        CommandAPI.onEnable();
        instance = this;
        String[] parts = Bukkit.getBukkitVersion().split("[^\\d.]")[0].split("\\.");
        int major = parts.length > 0 ? Integer.parseInt(parts[0]) : 0;
        int minor = parts.length > 1 ? Integer.parseInt(parts[1]) : 0;
        int patch = parts.length > 2 ? Integer.parseInt(parts[2]) : 0;
        Version version = Version.of(major, minor, patch);
        NMS = NMSFactory.createNMS(this, version);

        if (isPluginEnabled("Skript")) {
            addon = Skript.registerAddon(this);
            try {
                addon.loadClasses("it.jakegblp.nms.skript", "elements");
            } catch (IOException e) {
                e.printStackTrace();
            }
            getLogger().info("Hooked with Skript!");
        }
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
                            player.sendMessage("Spawned fake " + entityType + " at your location.");
                            getLogger().info("Spawned fake " + entityType + " at " + player.getName() + "'s location.");
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
                            player.sendMessage("Changed entity metadata of entity with id " + id);
                            getLogger().info("Changed entity metadata of entity with id " + id);
                            EntityMetadata entityMetadata = new EntityMetadata();
                            entityMetadata.set(MetadataKeyRegistries.LivingEntityKeys.HAND_STATES, new HandStates().setRiptiding(true));
                            entityMetadata.set(MetadataKeyRegistries.EntityKeys.ENTITY_FLAGS, new EntityFlags().setGlowing(true).setOnFire(true));
                            entityMetadata.set(MetadataKeyRegistries.EntityKeys.CUSTOM_NAME, name);
                            entityMetadata.set(MetadataKeyRegistries.EntityKeys.CUSTOM_NAME_VISIBILITY, true);
                            NMSApi.sendPacket(player, new EntityMetadataPacket(id, LivingEntity.class, entityMetadata));
                        }))
                .withSubcommand(new CommandAPICommand("player-rotation")
                        .withPermission("nms.command.packet.player_rotation")
                        .withArguments(new FloatArgument("pitch"), new FloatArgument("yaw"))
                        .executesPlayer((player, args) -> {
                            Float pitch = (Float) args.get(0),
                                    yaw = (Float) args.get(1);
                            player.sendMessage("Rotated " + player.getName() + " by pitch " + pitch + " and yaw " + yaw);
                            getLogger().info("Rotated " + player.getName() + " by pitch " + pitch + " and yaw " + yaw);
                            NMSApi.sendPacket(player, new PlayerRotationPacket(pitch, yaw));
                        }))
                .register();

        getLogger().info("NMS has been enabled!");
        getLogger().info("- Server version: " + version);
    }

    @Override
    public void onDisable() {
        CommandAPI.onDisable();
        if (NMS != null) {
            NMS = null;
        }
        getLogger().info("NMS has been disabled!");
    }

}
