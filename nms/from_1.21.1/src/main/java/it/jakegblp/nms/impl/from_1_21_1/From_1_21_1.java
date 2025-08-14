package it.jakegblp.nms.impl.from_1_21_1;

import it.jakegblp.nms.api.adapters.ResourceLocationAdapter;
import net.minecraft.resources.ResourceLocation;
import org.bukkit.NamespacedKey;

public class From_1_21_1 implements ResourceLocationAdapter<ResourceLocation> {

    @Override
    public ResourceLocation toNMSNamespacedKey(NamespacedKey namespacedKey) {
        return ResourceLocation.fromNamespaceAndPath(namespacedKey.getNamespace(), namespacedKey.getKey());
    }

    @Override
    public Class<ResourceLocation> getNMSNamespacedKeyClass() {
        return ResourceLocation.class;
    }
}