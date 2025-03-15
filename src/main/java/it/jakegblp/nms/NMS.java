package it.jakegblp.nms;

import com.github.zafarkhaja.semver.Version;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import lombok.Getter;

@Getter
public class NMS extends JavaPlugin {

    @Getter
    private static NMS instance;
    private Version serverVersion;
    @SuppressWarnings("rawtypes")
    private NMSAdapter nmsAdapter;

    @Override
    public void onEnable() {
        instance = this;
        serverVersion = Version.parse(Bukkit.getBukkitVersion()).toStableVersion();
        int
                major = (int) serverVersion.majorVersion(),
                minor = (int) serverVersion.minorVersion(),
                patch = (int) serverVersion.patchVersion();
        /*
         Fun Fact:
         The major version probably doesn't need a switch statement as Mojang will
         likely never make Minecraft 2.0, I'm keeping it for consistency with the minor and patch versions.
         */
        nmsAdapter = switch (major) {
            case 1 -> switch (minor) {
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
            default ->  null;
        };
        if (nmsAdapter == null) {
            throw new IllegalStateException("Unsupported Version: " + serverVersion);
        }
        getLogger().info("NMS has been enabled!");
        getLogger().info("- Server version: " + serverVersion);
    }

    @Override
    public void onDisable() {
        getLogger().info("NMS has been disabled!");
    }

}
