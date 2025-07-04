package it.jakegblp.nms.api.entity.metadata;

import it.jakegblp.nms.api.entity.metadata.keys.MetadataKey;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Pose;
import org.jetbrains.annotations.Nullable;

public interface EntityMetadataView {
    @Nullable EntityFlags getEntityFlags();
    @Nullable Integer getAirTicks();
    @Nullable Component getCustomName();
    @Nullable Boolean getCustomNameVisible();
    @Nullable Boolean getSilent();
    @Nullable Boolean getNoGravity();
    @Nullable Pose getPose();
    @Nullable Integer getTicksFrozen();
    <T> T get(MetadataKey<? extends Entity, T> key);
}