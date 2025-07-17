package it.jakegblp.nms.api.entity.metadata;

import it.jakegblp.nms.api.entity.metadata.key.MetadataKey;
import it.jakegblp.nms.api.entity.metadata.key.MetadataKeyRegistry;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Pose;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface EntityMetadataView {
    @Nullable EntityFlags getEntityFlags();
    @Nullable Integer getAirTicks();
    @Nullable Component getCustomName();
    @Nullable Boolean getCustomNameVisible();
    @Nullable Boolean getSilent();
    @Nullable Boolean getNoGravity();
    @Nullable Pose getPose();
    @Nullable Integer getTicksFrozen();

    @SuppressWarnings("unchecked")
    default <T> T get(MetadataKey<? extends Entity, T> key) {
        return (T) switch (key.getIndex()) {
            case 0 -> getEntityFlags();
            case 1 -> getAirTicks();
            case 2 -> getCustomName();
            case 3 -> getCustomNameVisible();
            case 4 -> getSilent();
            case 5 -> getNoGravity();
            case 6 -> getPose();
            case 7 -> getTicksFrozen();
            default ->
                    throw new IllegalArgumentException("Unrecognized key with index " + key.getIndex() + " for " + this.getClass().getSimpleName() + " metadata.");
        };
    }
    default Class<? extends Entity> getEntityClass() {
        return Entity.class;
    }

    default List<? extends MetadataKey<? extends Entity,?>> keys() {
        return MetadataKeyRegistry.getAllFor(getEntityClass(), true);
    }
}