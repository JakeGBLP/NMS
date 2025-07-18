package it.jakegblp.nms.api.entity.metadata.entity.livingentity;

import it.jakegblp.nms.api.entity.metadata.entity.EntityMetadataView;
import it.jakegblp.nms.api.entity.metadata.MetadataKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.BlockVector;
import org.jetbrains.annotations.Nullable;

public interface LivingEntityMetadataView extends EntityMetadataView {
    @Nullable HandStates getHandStates();
    @Nullable Float getHealth();
    @Nullable Integer getPotionEffectColor();
    @Nullable Boolean getPotionEffectAmbient();
    @Nullable Integer getArrowCount();
    @Nullable Integer getBeeStingerCount();
    @Nullable BlockVector getSleepingBedLocation();

    @Override
    @SuppressWarnings("unchecked")
    default <T> T get(MetadataKey<? extends Entity, T> key) {
        return (T) switch (key.getIndex()) {
            case 8 -> getHandStates();
            case 9 -> getHealth();
            case 10 -> getPotionEffectColor();
            case 11 -> getPotionEffectAmbient();
            case 12 -> getArrowCount();
            case 13 -> getBeeStingerCount();
            case 14 -> getSleepingBedLocation();
            default -> EntityMetadataView.super.get(key);
        };
    }

    @Override
    default Class<? extends Entity> getEntityClass() {
        return LivingEntity.class;
    }
}