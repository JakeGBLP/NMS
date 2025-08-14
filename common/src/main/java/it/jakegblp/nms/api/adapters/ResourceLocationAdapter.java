package it.jakegblp.nms.api.adapters;

import org.bukkit.NamespacedKey;

public interface ResourceLocationAdapter<NMSNamespacedKey> {
    default NamespacedKey fromNMSNamespacedKey(NMSNamespacedKey resourceLocation) {
        return NamespacedKey.fromString(resourceLocation.toString());
    }

    NMSNamespacedKey toNMSNamespacedKey(NamespacedKey namespacedKey);

    Class<NMSNamespacedKey> getNMSNamespacedKeyClass();

    default boolean isNMSNamespacedKey(Object object) {
        return getNMSNamespacedKeyClass().isInstance(object);
    }
}
