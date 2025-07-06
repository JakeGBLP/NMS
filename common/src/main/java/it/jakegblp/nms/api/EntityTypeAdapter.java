package it.jakegblp.nms.api;

import org.bukkit.entity.EntityType;

public interface EntityTypeAdapter {
    net.minecraft.world.entity.EntityType<?> from(EntityType from);
    EntityType to( net.minecraft.world.entity.EntityType<?> to);

}