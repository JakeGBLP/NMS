package it.jakegblp.nms.api.entity.metadata;

import org.bukkit.entity.Entity;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

/**
 * See: <a href="https://minecraft.wiki/w/Java_Edition_protocol/Entity_metadata#Entity">Minecraft Wiki – Entity Metadata</a>
 */
public record EntityMetadata(
        Map<MetadataKey<? extends Entity, ?>, Object> metadataItems,
        Class<? extends Entity> entityClass
) implements EntityMetadataView, Cloneable {

    public EntityMetadata {
        metadataItems = Map.copyOf(metadataItems);
    }

    public EntityMetadata() {
        this(Map.of());
    }

    @SuppressWarnings("unchecked")
    public EntityMetadata(Map<? extends MetadataKey<? extends Entity, ?>, Object> metadataItems) {
        this((Map<MetadataKey<? extends Entity, ?>, Object>) metadataItems, Entity.class);
    }

    public EntityMetadata(EntityMetadataView view) {
        this(view.metadataItems(), view.entityClass());
    }

    public <T> void set(MetadataKey<? extends Entity, ?> key, @Nullable T value) {
        metadataItems.put(key, value);
    }

    @SuppressWarnings("CloneDoesntCallSuperClone")
    @Override
    public EntityMetadata clone() {
        return new EntityMetadata(this);
    }
}