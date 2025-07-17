package it.jakegblp.nms.api;

import org.bukkit.entity.EntityType;

public interface EntityTypeAdapter<
        NMSEntityType
        > {
    NMSEntityType toNMSEntityType(EntityType from);
    EntityType toEntityType(NMSEntityType to);

}