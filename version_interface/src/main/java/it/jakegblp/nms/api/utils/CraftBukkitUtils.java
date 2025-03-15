package it.jakegblp.nms.api.utils;

import org.bukkit.Bukkit;

public interface CraftBukkitUtils {
    String CRAFTBUKKIT_PACKAGE = Bukkit.getServer().getClass().getPackage().getName();

    /**
     * @param endingPart the ending part of the class path (Ex: "entity.CraftPlayer", must not start with a '.')
     * @return the craftbukkit class
     */
    static Class<?> getCraftBukkitClass(String endingPart) {
        return ReflectionUtils.getCachedClass(CRAFTBUKKIT_PACKAGE + "." + endingPart);
    }
}
