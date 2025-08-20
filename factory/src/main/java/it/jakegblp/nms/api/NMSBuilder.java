package it.jakegblp.nms.api;

import it.jakegblp.nms.api.adapters.*;
import lombok.Setter;
import org.bukkit.plugin.java.JavaPlugin;

import static it.jakegblp.nms.api.utils.ReflectionUtils.*;

@Setter
public class NMSBuilder {

    private final int major, minor, patch;
    private JavaPlugin plugin;
    private EntityTypeAdapter<?> entityTypeAdapter;
    private MajorChangesAdapter majorChangesAdapter;
    private ResourceLocationAdapter<?> resourceLocationAdapter;
    private EntitySpawnPacketAdapter<?> entitySpawnPacketAdapter;
    private EntityMetadataPacketAdapter<?> entityMetadataPacketAdapter;
    private PlayerRotationPacketAdapter playerRotationPacketAdapter;
    private ClientBundlePacketAdapter<?> clientBundlePacketAdapter;
    private SetEquipmentPacketAdapter<?> setEquipmentPacketAdapter;

    public NMSBuilder(int major, int minor, int patch) {
        this.major = major;
        this.minor = minor;
        this.patch = patch;
    }

    public AbstractNMS<?> build() {
        String version = "v" + major + "_" + minor + (patch == 0 ? "" : "_" + patch);
        return (AbstractNMS<?>) newInstance(
                getDeclaredConstructor(
                        forClassName("it.jakegblp.nms.impl." + version + "." + version),
                        JavaPlugin.class,
                        EntityTypeAdapter.class,
                        MajorChangesAdapter.class,
                        ResourceLocationAdapter.class,
                        EntitySpawnPacketAdapter.class,
                        EntityMetadataPacketAdapter.class,
                        PlayerRotationPacketAdapter.class,
                        ClientBundlePacketAdapter.class,
                        SetEquipmentPacketAdapter.class
                ),
                plugin,
                entityTypeAdapter,
                majorChangesAdapter,
                resourceLocationAdapter,
                entitySpawnPacketAdapter,
                entityMetadataPacketAdapter,
                playerRotationPacketAdapter,
                clientBundlePacketAdapter,
                setEquipmentPacketAdapter
        );
    }


}
