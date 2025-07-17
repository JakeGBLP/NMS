package it.jakegblp.nms.impl.from_1_18_to_1_20_6;

import it.jakegblp.nms.api.ResourceLocationAdapter;
import net.minecraft.resources.ResourceLocation;
import org.bukkit.NamespacedKey;

public class From_1_18_To_1_20_6 implements ResourceLocationAdapter<ResourceLocation> {

    @Override
    public ResourceLocation asResourceLocation(NamespacedKey namespacedKey) {
        return new ResourceLocation(namespacedKey.getNamespace(), namespacedKey.getKey());
    }

}