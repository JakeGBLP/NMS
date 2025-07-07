package it.jakegblp.nms.api;

import com.github.zafarkhaja.semver.Version;
import it.jakegblp.nms.api.entity.metadata.key.MetadataKeyRegistry;
import it.jakegblp.nms.impl.*;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.plugin.java.JavaPlugin;

import static com.github.zafarkhaja.semver.Version.of;
import static it.jakegblp.nms.api.NMSAdapter.NMS;

public class NMSFactory {

    public static NMSAdapter createNMS(
            JavaPlugin javaPlugin,
            Version minecraftVersion
    ) {
        int
                major = (int) minecraftVersion.majorVersion(),
                minor = (int) minecraftVersion.minorVersion(),
                patch = (int) minecraftVersion.patchVersion();
        /*
         Fun Fact:
         The major version probably doesn't need a switch statement as Mojang will
         likely never make Minecraft 2.0, I'm keeping it for consistency with the minor and patch versions.
         */
        if (major == 1) {
            NMS = switch (minor) {
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
                    case 5 -> new v1_21_5();
                    case 6 -> new v1_21_6();
                    case 7 -> new v1_21_7();
                    default -> null;
                };
                default -> null;
            };
            if (NMS != null) {
                if (minecraftVersion.isHigherThanOrEquivalentTo(of(1, 19, 3))) {
                    From_1_19_3 from_1_19_3 = new From_1_19_3();
                    NMS.entityTypeAdapter = from_1_19_3;
                    NMS.entityMetadataPacketAdapter = from_1_19_3;
                } else {
                    To_1_19_1 from_1_19_1 = new To_1_19_1();
                    NMS.entityTypeAdapter = from_1_19_1;
                    NMS.entityMetadataPacketAdapter = from_1_19_1;
                }
                if (minecraftVersion.isHigherThanOrEquivalentTo(of(1, 19)))
                    NMS.entitySpawnPacketAdapter = new From_1_19();
                else
                    NMS.entitySpawnPacketAdapter = new To_1_18_2();
                if (minecraftVersion.isHigherThanOrEquivalentTo(of(1, 21, 3))) {
                    NMS.playerRotationPacketAdapter = new From_1_21_3();
                }
            }
        } else {
            NMS = null;
        }
        MetadataKeyRegistry.init();

        if (NMS == null)
            throw new IllegalStateException("NMS implementation not found");
        NMS.adventure = BukkitAudiences.create(javaPlugin);
        return NMS;
    }
}
