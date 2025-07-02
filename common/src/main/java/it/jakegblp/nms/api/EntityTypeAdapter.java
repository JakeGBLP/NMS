package it.jakegblp.nms.api;

import org.bukkit.entity.EntityType;

public interface EntityTypeAdapter<NMS> {
    NMS from(EntityType from);
    EntityType to(NMS to);

}