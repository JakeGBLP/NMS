package it.jakegblp.nms.api;

import org.bukkit.NamespacedKey;

public interface ResourceLocationAdapter<NMSResourceLocation> {
    default NamespacedKey asNamespacedKey(NMSResourceLocation resourceLocation) {
        return NamespacedKey.fromString(resourceLocation.toString());
    }
    NMSResourceLocation asResourceLocation(NamespacedKey namespacedKey);
}
