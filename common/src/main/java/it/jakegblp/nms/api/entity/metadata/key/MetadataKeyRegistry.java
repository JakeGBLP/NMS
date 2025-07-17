package it.jakegblp.nms.api.entity.metadata.key;

import it.jakegblp.nms.api.entity.metadata.EntityFlags;
import it.jakegblp.nms.api.entity.metadata.EntitySerializerInfo;
import it.jakegblp.nms.api.entity.metadata.HandStates;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Pose;
import org.bukkit.util.BlockVector;

import java.util.*;

public final class MetadataKeyRegistry {
    private static final Map<Class<? extends Entity>, List<MetadataKey<?, ?>>> keysByClass = new HashMap<>();

    public static <E extends Entity, T> MetadataKey<E, T> register(MetadataKey<E, T> key) {
        keysByClass.computeIfAbsent(key.getEntityClass(), c -> new ArrayList<>()).add(key);
        return key;
    }

    @SuppressWarnings("unchecked")
    public static <E extends Entity> List<MetadataKey<E, ?>> getAllFor(Class<E> entityClass, boolean includeSuperTypes) {
        Set<MetadataKey<E, ?>> result = new TreeSet<>(Comparator.comparingInt(MetadataKey::getIndex));
        Queue<Class<?>> queue = new ArrayDeque<>();
        queue.add(entityClass);

        while (!queue.isEmpty()) {
            Class<?> current = queue.poll();
            List<MetadataKey<?, ?>> keys = keysByClass.get(current);
            if (keys != null)
                for (MetadataKey<?, ?> key : keys)
                    result.add((MetadataKey<E, ?>) key);
            if (includeSuperTypes) {
                Class<?> superclass = current.getSuperclass();
                if (superclass != null && Entity.class.isAssignableFrom(superclass))
                    queue.add(superclass);
                for (Class<?> interfaceClass : current.getInterfaces())
                    if (Entity.class.isAssignableFrom(interfaceClass))
                        queue.add(interfaceClass);
            }
        }

        return new ArrayList<>(result);
    }

    public static <E extends Entity> List<MetadataKey<E, ?>> getAllFor(Class<E> entityClass) {
        return getAllFor(entityClass, false);
    }

    public static void init() {
        EntityKeys.init();
        LivingEntityKeys.init();
    }

    public static final class EntityKeys {
        public static final MetadataKey<Entity, EntityFlags> ENTITY_FLAGS = register(new MetadataKey<>(Entity.class, 0, new EntityFlags()));
        public static final MetadataKey<Entity, Integer> AIR_TICKS = register(new MetadataKey<>(Entity.class, 1, 300));
        public static final MetadataKey<Entity, Component> CUSTOM_NAME = register(new MetadataKey<>(Entity.class, 2, null, Component.class, EntitySerializerInfo.Type.OPTIONAL));
        public static final MetadataKey<Entity, Boolean> CUSTOM_NAME_VISIBILITY = register(new MetadataKey<>(Entity.class, 3, false));
        public static final MetadataKey<Entity, Boolean> SILENT = register(new MetadataKey<>(Entity.class, 4, false));
        public static final MetadataKey<Entity, Boolean> NO_GRAVITY = register(new MetadataKey<>(Entity.class, 5, false));
        public static final MetadataKey<Entity, Pose> POSE = register(new MetadataKey<>(Entity.class, 6, Pose.STANDING));
        public static final MetadataKey<Entity, Integer> TICKS_FROZEN = register(new MetadataKey<>(Entity.class, 7, 0));

        public static List<MetadataKey<Entity, ?>> keys() {
            return MetadataKeyRegistry.getAllFor(Entity.class);
        }

        public static void init() {}
    }

    public static final class LivingEntityKeys {
        public static final MetadataKey<LivingEntity, HandStates> HAND_STATES = register(new MetadataKey<>(LivingEntity.class, 8, new HandStates()));
        public static final MetadataKey<LivingEntity, Float> HEALTH = register(new MetadataKey<>(LivingEntity.class, 9, 1f));
        public static final MetadataKey<LivingEntity, Integer> POTION_EFFECT_COLOR = register(new MetadataKey<>(LivingEntity.class, 10, 0));
        public static final MetadataKey<LivingEntity, Boolean> POTION_EFFECT_AMBIENT = register(new MetadataKey<>(LivingEntity.class, 11, false));
        public static final MetadataKey<LivingEntity, Integer> ARROW_COUNT = register(new MetadataKey<>(LivingEntity.class, 12, 0));
        public static final MetadataKey<LivingEntity, Integer> BEE_STINGER_COUNT = register(new MetadataKey<>(LivingEntity.class, 13, 0));
        public static final MetadataKey<LivingEntity, BlockVector> SLEEPING_BED_LOCATION = register(new MetadataKey<>(LivingEntity.class, 14, null, BlockVector.class, EntitySerializerInfo.Type.OPTIONAL));

        public static List<MetadataKey<LivingEntity, ?>> keys() {
            return MetadataKeyRegistry.getAllFor(LivingEntity.class);
        }

        public static void init() {}
    }


}