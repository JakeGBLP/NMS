package it.jakegblp.nms.api;

import com.github.zafarkhaja.semver.Version;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public class NMS extends JavaPlugin {

    private static NMS instance;
    private Version serverVersion;

    @Override
    public void onEnable() {
        instance = this;
        serverVersion = Version.parse(Bukkit.getVersion());
        getLogger().info("NMS has been enabled! Server version: " + serverVersion);
    }

    @Override
    public void onDisable() {
        getLogger().info("NMS has been disabled!");
    }

}
