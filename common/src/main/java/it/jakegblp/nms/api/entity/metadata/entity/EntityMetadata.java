package it.jakegblp.nms.api.entity.metadata.entity;

import it.jakegblp.nms.api.entity.metadata.MetadataKey;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Pose;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static it.jakegblp.nms.api.utils.NullabilityUtils.cloneIfNotNull;

/**
 * See: <a href="https://minecraft.wiki/w/Java_Edition_protocol/Entity_metadata#Entity">Minecraft Wiki – Entity Metadata</a>
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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
        return getMetadataItems(keys());
    }

    public Map<MetadataKey<? extends Entity, ?>, Object> getMetadataItems(List<? extends MetadataKey<? extends Entity,?>> keys) {
        Map<MetadataKey<? extends Entity, ?>, Object> map = new TreeMap<>(Comparator.comparingInt(MetadataKey::getIndex));
        for (MetadataKey<? extends Entity, ?> key : keys) {
            Object value = get(key);
            if (value != null)
                map.put(key, value);
        }
        return map;
    }

    @SuppressWarnings("CloneDoesntCallSuperClone")
    @Override
    public EntityMetadata clone() {
        return new EntityMetadata(this);
    }

}