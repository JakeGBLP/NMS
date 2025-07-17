package it.jakegblp.nms.impl.from_1_21_1;

import it.jakegblp.nms.api.ResourceLocationAdapter;
import net.minecraft.resources.ResourceLocation;
import org.bukkit.NamespacedKey;

public class From_1_21_1 implements ResourceLocationAdapter<ResourceLocation> {

    @Override
    public ResourceLocation asResourceLocation(NamespacedKey namespacedKey) {
        return ResourceLocation.fromNamespaceAndPath(namespacedKey.getNamespace(), namespacedKey.getKey());
    }
}