package it.jakegblp.nms.api.entity.metadata;

import it.jakegblp.nms.api.entity.metadata.wrappers.EntityFlags;
import it.jakegblp.nms.api.entity.metadata.wrappers.HandStates;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.AreaEffectCloud;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Pose;
import org.bukkit.util.BlockVector;
import org.jetbrains.annotations.UnmodifiableView;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

public final class MetadataKeyRegistries {

    private static final Map<Class<? extends Entity>, List<MetadataKey.Named<?, ?>>> keysByClass = getFromInnerClassFieldsFields(MetadataKeyRegistries.class);

    public static Map<Class<? extends Entity>, List<MetadataKey.Named<?, ?>>> getFromInnerClassFieldsFields(Class<?> theClass) {
        Map<Class<? extends Entity>, List<MetadataKey.Named<?, ?>>> map = new HashMap<>();
        for (Class<?> declaredClass : theClass.getDeclaredClasses()) {
            int classModifiers = declaredClass.getModifiers();
            if (Modifier.isStatic(classModifiers) && Modifier.isFinal(classModifiers)) {
                for (Field declaredField : declaredClass.getDeclaredFields()) {
                    int fieldModifiers = declaredField.getModifiers();
                    Class<?> type = declaredField.getType();
                    if (type.equals(MetadataKey.class) && Modifier.isStatic(fieldModifiers) && Modifier.isFinal(fieldModifiers)) {
                        MetadataKey<?, ?> metadataKey;
                        try {
                            metadataKey = (MetadataKey<?, ?>) declaredField.get(null);
                        } catch (IllegalAccessException e) {
                            throw new RuntimeException(e);
                        }
                        map.computeIfAbsent(metadataKey.getEntityClass(), c -> new ArrayList<>()).add(MetadataKey.named(declaredField.getName(), metadataKey));
                    }
                }
            }
        }
        return map;
    }

    @SuppressWarnings("unchecked")
    public static <E extends Entity> List<MetadataKey.Named<E, ?>> getAllFor(Class<E> entityClass, boolean includeSuperTypes) {
        Set<MetadataKey.Named<E, ?>> result = new TreeSet<>(Comparator.comparingInt(MetadataKey::getIndex));
        Queue<Class<?>> queue = new ArrayDeque<>();
        queue.add(entityClass);

        while (!queue.isEmpty()) {
            Class<?> current = queue.poll();
            List<MetadataKey.Named<E, ?>> keys = (List<MetadataKey.Named<E, ?>>) (List<?>) keysByClass.get(current);
            if (keys != null)
                result.addAll(keys);
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

    public static <E extends Entity> List<MetadataKey.Named<E, ?>> getAllFor(Class<E> entityClass) {
        return getAllFor(entityClass, false);
    }

    public static void init() {
        EntityKeys.init();
        LivingEntityKeys.init();
    }

    @UnmodifiableView
    public static Map<Class<? extends Entity>, List<MetadataKey.Named<?, ?>>> getRegistry() {
        //    var value = entry.getValue();
        //    return entry.getKey().getSimpleName() + ": [\n\t\t\t\t"+ value.stream()
        //            .map(namedKey -> "{ name: \"" + namedKey.getName()+ "\", index: "+ namedKey.getIndex()+", value: "+namedKey.getValue()+" }").collect(Collectors.joining(",\n\t\t\t\t"))+"\n\t\t]";
        //}).collect(Collectors.joining(",\n\t\t"))+"\n}");
        return Collections.unmodifiableMap(keysByClass);
    }

    public static final class EntityKeys {
        public static final MetadataKey<Entity, EntityFlags> ENTITY_FLAGS = new MetadataKey<>(Entity.class, 0, new EntityFlags());
        public static final MetadataKey<Entity, Integer> AIR_TICKS = new MetadataKey<>(Entity.class, 1, 300);
        public static final MetadataKey<Entity, Component> CUSTOM_NAME = new MetadataKey<>(Entity.class, 2, null, Component.class, EntitySerializerInfo.Type.OPTIONAL);
        public static final MetadataKey<Entity, Boolean> CUSTOM_NAME_VISIBILITY = new MetadataKey<>(Entity.class, 3, false);
        public static final MetadataKey<Entity, Boolean> SILENT = new MetadataKey<>(Entity.class, 4, false);
        public static final MetadataKey<Entity, Boolean> NO_GRAVITY = new MetadataKey<>(Entity.class, 5, false);
        public static final MetadataKey<Entity, Pose> POSE = new MetadataKey<>(Entity.class, 6, Pose.STANDING);
        public static final MetadataKey<Entity, Integer> TICKS_FROZEN = new MetadataKey<>(Entity.class, 7, 0);

        public static List<MetadataKey.Named<Entity, ?>> keys() {
            return MetadataKeyRegistries.getAllFor(Entity.class);
        }

        public static void init() {
        }
    }

    public static final class LivingEntityKeys {
        public static final MetadataKey<LivingEntity, HandStates> HAND_STATES = new MetadataKey<>(LivingEntity.class, 8, new HandStates());
        public static final MetadataKey<LivingEntity, Float> HEALTH = new MetadataKey<>(LivingEntity.class, 9, 1f);
        public static final MetadataKey<LivingEntity, Integer> POTION_EFFECT_COLOR = new MetadataKey<>(LivingEntity.class, 10, 0);
        public static final MetadataKey<LivingEntity, Boolean> POTION_EFFECT_AMBIENT = new MetadataKey<>(LivingEntity.class, 11, false);
        public static final MetadataKey<LivingEntity, Integer> ARROW_COUNT = new MetadataKey<>(LivingEntity.class, 12, 0);
        public static final MetadataKey<LivingEntity, Integer> BEE_STINGER_COUNT = new MetadataKey<>(LivingEntity.class, 13, 0);
        public static final MetadataKey<LivingEntity, BlockVector> SLEEPING_BED_LOCATION = new MetadataKey<>(LivingEntity.class, 14, null, BlockVector.class, EntitySerializerInfo.Type.OPTIONAL);

        public static List<MetadataKey.Named<LivingEntity, ?>> keys() {
            return MetadataKeyRegistries.getAllFor(LivingEntity.class);
        }

        public static void init() {
        }
    }

    public static final class AreaEffectCloudKeys {
        public static final MetadataKey<AreaEffectCloud, Float> HEALTH = new MetadataKey<>(AreaEffectCloud.class, 8, 3f);
        public static final MetadataKey<AreaEffectCloud, Boolean> POTION_EFFECT_AMBIENT = new MetadataKey<>(AreaEffectCloud.class, 9, false);
        public static final MetadataKey<AreaEffectCloud, Integer> ARROW_COUNT = new MetadataKey<>(AreaEffectCloud.class, 10, -1);

        public static List<MetadataKey.Named<AreaEffectCloud, ?>> keys() {
            return MetadataKeyRegistries.getAllFor(AreaEffectCloud.class);
        }

        public static void init() {
        }
    }

}