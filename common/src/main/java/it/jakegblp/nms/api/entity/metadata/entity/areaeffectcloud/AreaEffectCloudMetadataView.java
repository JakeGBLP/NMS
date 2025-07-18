package it.jakegblp.nms.api.entity.metadata.entity.areaeffectcloud;

import it.jakegblp.nms.api.entity.metadata.MetadataKey;
import it.jakegblp.nms.api.entity.metadata.entity.EntityMetadataView;
import org.bukkit.entity.AreaEffectCloud;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.Nullable;

public interface AreaEffectCloudMetadataView extends EntityMetadataView {
    @Nullable Float getRadius();
    @Nullable Boolean getIgnoreRadius();
    @Nullable Integer getParticle();

    @Override
    @SuppressWarnings("unchecked")
    default <T> T get(MetadataKey<? extends Entity, T> key) {
        return (T) switch (key.getIndex()) {
            case 8 -> getRadius();
            case 9 -> getIgnoreRadius();
            case 10 -> getParticle();
            default -> EntityMetadataView.super.get(key);
        };
    }

    @Override
    default Class<? extends Entity> getEntityClass() {
        return AreaEffectCloud.class;
    }
}