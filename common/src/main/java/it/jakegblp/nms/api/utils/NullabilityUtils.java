package it.jakegblp.nms.api.utils;

import it.jakegblp.nms.api.entity.metadata.entity.EntityFlags;
import it.jakegblp.nms.api.entity.metadata.entity.livingentity.HandStates;
import org.bukkit.util.BlockVector;

public class NullabilityUtils {
    @SuppressWarnings("unchecked")
    public static <T> T cloneIfNotNull(T obj) {
        if (obj == null) return null;
        else if (obj instanceof EntityFlags entityFlags) return (T) entityFlags.clone();
        else if (obj instanceof HandStates handStates) return (T) handStates.clone();
        else if (obj instanceof BlockVector blockVector) return (T) blockVector.clone();
        throw new UnsupportedOperationException("Cannot clone " + obj.getClass().getName() + " comfortably!");
    }
}
