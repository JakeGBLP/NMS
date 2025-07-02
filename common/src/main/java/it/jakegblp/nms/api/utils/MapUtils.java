package it.jakegblp.nms.api.utils;

import java.util.HashMap;
import java.util.Map;

public interface MapUtils {
    @SafeVarargs
    static <K, V> HashMap<K, V> createEntityDataMap(
            Map.Entry<K, V>... entries
    ) {
        HashMap<K, V> map = new HashMap<>();
        for (Map.Entry<K, V> entry : entries)
            map.put(entry.getKey(), entry.getValue());
        return map;
    }
}
