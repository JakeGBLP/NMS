package it.jakegblp.nms.api.entity.metadata;

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
}