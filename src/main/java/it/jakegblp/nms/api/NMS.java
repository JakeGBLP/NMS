package it.jakegblp.nms.api;

import com.github.zafarkhaja.semver.Version;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import lombok.Getter;

@Getter
public class NMS extends JavaPlugin {

    @Getter
    private static NMS instance;
    private Version serverVersion;

    @Override
    public void onEnable() {
        instance = this;
        serverVersion = Version.parse(Bukkit.getBukkitVersion()).toStableVersion();
        getLogger().info("NMS has been enabled! Server version: " + serverVersion);
    }

    @Override
    public void onDisable() {
        getLogger().info("NMS has been disabled!");
    }

}
