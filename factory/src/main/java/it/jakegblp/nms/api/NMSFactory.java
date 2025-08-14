package it.jakegblp.nms.api;

import com.github.zafarkhaja.semver.Version;
import it.jakegblp.nms.impl.from_1_18.From_1_18;
import it.jakegblp.nms.impl.from_1_18_to_1_18_2.From_1_18_To_1_18_2;
import it.jakegblp.nms.impl.from_1_18_to_1_19_1.From_1_18_To_1_19_1;
import it.jakegblp.nms.impl.from_1_18_to_1_20_6.From_1_18_To_1_20_6;
import it.jakegblp.nms.impl.from_1_19.From_1_19;
import it.jakegblp.nms.impl.from_1_19_3.From_1_19_3;
import it.jakegblp.nms.impl.from_1_19_4.From_1_19_4;
import it.jakegblp.nms.impl.from_1_21_1.From_1_21_1;
import it.jakegblp.nms.impl.from_1_21_3.From_1_21_3;
import it.jakegblp.nms.impl.to_1_17_1.To_1_17_1;
import org.bukkit.plugin.java.JavaPlugin;

import static com.github.zafarkhaja.semver.Version.of;

public class NMSFactory {

    public static AbstractNMS<?> createNMS(
            JavaPlugin javaPlugin,
            Version minecraftVersion
    ) {
        int
                major = (int) minecraftVersion.majorVersion(),
                minor = (int) minecraftVersion.minorVersion(),
                patch = (int) minecraftVersion.patchVersion();
        if (major == 1) {
            switch (minor) {
                case 19 -> {
                    if (patch == 2) patch = 1;
                }
                case 20 -> {
                    switch (patch) {
                        case 0 -> patch = 1;
                        case 3 -> patch = 4;
                        case 5 -> patch = 6;
                    }
                }
                case 21 -> {
                    switch (patch) {
                        case 0 -> patch = 1;
                        case 2 -> patch = 3;
                    }
                }
            }
        }

        NMSBuilder<?> nmsBuilder = new NMSBuilder<>(major, minor, patch, javaPlugin);
        To_1_17_1 to_1_17_1 = null;
        boolean atLeast_1_18 = minecraftVersion.isHigherThanOrEquivalentTo(of(1, 18));
        // Conversion Adapter:
        // <= 1.17.1
        // >= 1.18
        if (atLeast_1_18)
            nmsBuilder.conversionAdapter(new From_1_18());
        else {
            to_1_17_1 = new To_1_17_1();
            nmsBuilder.conversionAdapter(to_1_17_1);
        }

        // Conversion Adapter:
        // >= 1.21.1
        // 1.20.6 - 1.18
        // 1.17.1
        if (minecraftVersion.isHigherThanOrEquivalentTo(of(1, 21, 1)))
            nmsBuilder.resourceLocationAdapter(new From_1_21_1());
        else if (atLeast_1_18)
            nmsBuilder.resourceLocationAdapter(new From_1_18_To_1_20_6());
        else
            nmsBuilder.resourceLocationAdapter(to_1_17_1);

        if (minecraftVersion.isHigherThanOrEquivalentTo(of(1, 19, 3))) {
            From_1_19_3 from_1_19_3 = new From_1_19_3();
            nmsBuilder.entityTypeAdapter(from_1_19_3);
            nmsBuilder.entityMetadataPacketAdapter(from_1_19_3);
        } else if (atLeast_1_18) {
            From_1_18_To_1_19_1 from1_18_To_1_19_1 = new From_1_18_To_1_19_1();
            nmsBuilder.entityTypeAdapter(from1_18_To_1_19_1);
            nmsBuilder.entityMetadataPacketAdapter(from1_18_To_1_19_1);
        } else {
            nmsBuilder.entityTypeAdapter(to_1_17_1);
            nmsBuilder.entityMetadataPacketAdapter(to_1_17_1);
        }

        if (minecraftVersion.isHigherThanOrEquivalentTo(of(1, 19)))
            nmsBuilder.entitySpawnPacketAdapter(new From_1_19());
        else if (atLeast_1_18)
            nmsBuilder.entitySpawnPacketAdapter(new From_1_18_To_1_18_2());
        else
            nmsBuilder.entitySpawnPacketAdapter(to_1_17_1);

        if (minecraftVersion.isHigherThanOrEquivalentTo(of(1, 21, 3)))
            nmsBuilder.playerRotationPacketAdapter(new From_1_21_3());

        if (minecraftVersion.isHigherThanOrEquivalentTo(of(1, 19, 4)))
            nmsBuilder.bundleDelimiterPacketAdapter(new From_1_19_4());
        return nmsBuilder.build();
    }
}
