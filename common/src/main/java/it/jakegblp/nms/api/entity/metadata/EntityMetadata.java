package it.jakegblp.nms.api.entity.metadata;

import it.jakegblp.nms.api.entity.metadata.key.MetadataKey;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Pose;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

import static it.jakegblp.nms.api.entity.metadata.key.MetadataKeyRegistry.EntityKeys.keys;
import static it.jakegblp.nms.api.utils.NullabilityUtils.cloneIfNotNull;

/**
 * See: <a href="https://minecraft.wiki/w/Java_Edition_protocol/Entity_metadata#Entity">Minecraft Wiki – Entity Metadata</a>
 */
@Getter
@Setter
@AllArgsConstructor
@SuppressWarnings("unchecked")
@SuperBuilder(toBuilder = true)
public class EntityMetadata implements EntityMetadataView, Cloneable {

    @Nullable
    protected EntityFlags entityFlags;
    @Nullable
    protected Integer airTicks;
    @Nullable
    protected Component customName;
    @Nullable
    protected Boolean customNameVisible;
    @Nullable
    protected Boolean silent;
    @Nullable
    protected Boolean noGravity;
    @Nullable
    protected Pose pose;
    @Nullable
    protected Integer ticksFrozen;

    /**
     * Creates an entity metadata instance where all values are null.
     */
    public EntityMetadata() {
    }

    public EntityMetadata(EntityMetadataView view) {
        this(
                cloneIfNotNull(view.getEntityFlags()),
                view.getAirTicks(),
                view.getCustomName(),
                view.getCustomNameVisible(),
                view.getSilent(),
                view.getNoGravity(),
                view.getPose(),
                view.getTicksFrozen()
        );
    }

    @Override
    public <T> T get(MetadataKey<? extends Entity, T> key) {
        return (T) switch (key.getIndex()) {
            case 0 -> entityFlags;
            case 1 -> airTicks;
            case 2 -> customName;
            case 3 -> customNameVisible;
            case 4 -> silent;
            case 5 -> noGravity;
            case 6 -> pose;
            case 7 -> ticksFrozen;
            default ->
                    throw new IllegalArgumentException("Unrecognized key with index " + key.getIndex() + " for " + this.getClass().getSimpleName() + " metadata.");
        };
    }

    public <T> void set(MetadataKey<? extends Entity, T> key, @Nullable T value) {
        switch (key.getIndex()) {
            case 0 -> entityFlags = (EntityFlags) value;
            case 1 -> airTicks = (Integer) value;
            case 2 -> customName = (Component) value;
            case 3 -> customNameVisible = (Boolean) value;
            case 4 -> silent = (Boolean) value;
            case 5 -> noGravity = (Boolean) value;
            case 6 -> pose = (Pose) value;
            case 7 -> ticksFrozen = (Integer) value;
            default ->
                    throw new IllegalArgumentException("Unrecognized key with index " + key.getIndex() + " for " + this.getClass().getSimpleName() + " metadata.");
        }
    }

    public Map<MetadataKey<? extends Entity, ?>, Object> getMetadataItems() {
        Map<MetadataKey<? extends Entity, ?>, Object> map = new TreeMap<>(Comparator.comparingInt(MetadataKey::getIndex));
        for (MetadataKey<Entity, ?> key : keys())
            map.put(key, get(key));
        return map;
    }

    @Override
    public EntityMetadata clone() {
        return new EntityMetadata(this);
    }

}