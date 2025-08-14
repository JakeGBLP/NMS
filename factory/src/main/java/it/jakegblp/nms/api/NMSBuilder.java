package it.jakegblp.nms.api;

import it.jakegblp.nms.api.adapters.*;
import org.bukkit.plugin.java.JavaPlugin;

import static it.jakegblp.nms.api.utils.ReflectionUtils.*;

public class NMSBuilder<B extends NMSBuilder<B>> {

    private final int major, minor, patch;
    private JavaPlugin plugin;
    private EntityTypeAdapter<?> entityTypeAdapter;
    private MajorChangesAdapter<?, ?, ?, ?, ?, ?, ?, ?, ?> majorChangesAdapter;
    private ResourceLocationAdapter<?> resourceLocationAdapter;
    private EntitySpawnPacketAdapter<?> entitySpawnPacketAdapter;
    private EntityMetadataPacketAdapter<?> entityMetadataPacketAdapter;
    private PlayerRotationPacketAdapter playerRotationPacketAdapter;
    private BundleDelimiterPacketAdapter<?> bundleDelimiterPacketAdapter;

    public NMSBuilder(int major, int minor, int patch) {
        this.major = major;
        this.minor = minor;
        this.patch = patch;
    }

    public NMSBuilder(int major, int minor, int patch, JavaPlugin plugin) {
        this(major, minor, patch);
        this.plugin = plugin;
    }

    public NMSBuilder<B> entityTypeAdapter(EntityTypeAdapter<?> entityTypeAdapter) {
        this.entityTypeAdapter = entityTypeAdapter;
        return this;
    }

    public NMSBuilder<B> conversionAdapter(MajorChangesAdapter<?, ?, ?, ?, ?, ?, ?, ?, ?> majorChangesAdapter) {
        this.majorChangesAdapter = majorChangesAdapter;
        return this;
    }

    public NMSBuilder<B> resourceLocationAdapter(ResourceLocationAdapter<?> resourceLocationAdapter) {
        this.resourceLocationAdapter = resourceLocationAdapter;
        return this;
    }

    public NMSBuilder<B> entitySpawnPacketAdapter(EntitySpawnPacketAdapter<?> entitySpawnPacketAdapter) {
        this.entitySpawnPacketAdapter = entitySpawnPacketAdapter;
        return this;
    }

    public NMSBuilder<B> entityMetadataPacketAdapter(EntityMetadataPacketAdapter<?> entityMetadataPacketAdapter) {
        this.entityMetadataPacketAdapter = entityMetadataPacketAdapter;
        return this;
    }

    public NMSBuilder<B> playerRotationPacketAdapter(PlayerRotationPacketAdapter playerRotationPacketAdapter) {
        this.playerRotationPacketAdapter = playerRotationPacketAdapter;
        return this;
    }

    public NMSBuilder<B> bundleDelimiterPacketAdapter(BundleDelimiterPacketAdapter<?> bundleDelimiterPacketAdapter) {
        this.bundleDelimiterPacketAdapter = bundleDelimiterPacketAdapter;
        return this;
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
                        BundleDelimiterPacketAdapter.class
                ),
                plugin,
                entityTypeAdapter,
                majorChangesAdapter,
                resourceLocationAdapter,
                entitySpawnPacketAdapter,
                entityMetadataPacketAdapter,
                playerRotationPacketAdapter,
                bundleDelimiterPacketAdapter
        );
    }


}
