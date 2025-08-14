package it.jakegblp.nms.api.entity.metadata;

import org.bukkit.entity.Entity;

import java.util.Map;

import static it.jakegblp.nms.api.utils.NullabilityUtils.cloneIfNotNull;

public interface EntityMetadataView {

    default void transferTo(EntityMetadata to, MetadataKey<? extends Entity, Object> key) {
        to.set(key, cloneIfNotNull(get(key)));
    }

    @SuppressWarnings("unchecked")
    default <T> T get(MetadataKey<? extends Entity, T> key) {
        return (T) metadataItems().get(key);
    }

    Class<? extends Entity> entityClass();

    default int size() {
        return metadataItems().size();
    }

    Map<MetadataKey<? extends Entity, ?>, Object> metadataItems();

}