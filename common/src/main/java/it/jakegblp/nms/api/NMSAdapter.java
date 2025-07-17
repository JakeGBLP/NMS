package it.jakegblp.nms.api;

import com.google.common.base.Preconditions;
import it.jakegblp.nms.api.entity.metadata.EntitySerializerInfo;
import it.jakegblp.nms.api.entity.metadata.Flags;
import it.jakegblp.nms.api.entity.metadata.key.MetadataKey;
import lombok.experimental.Delegate;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.entity.Pose;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.BlockVector;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

// builder?
public abstract class NMSAdapter<
        EntityDataSerializer
        > {

    public static NMSAdapter<?> NMS;

    public JavaPlugin javaPlugin;
    @Delegate
    public EntityTypeAdapter entityTypeAdapter;
    @Delegate
    public ConversionAdapter conversionAdapter;
    @Delegate
    public ResourceLocationAdapter resourceLocationAdapter;
    @Delegate
    public EntitySpawnPacketAdapter entitySpawnPacketAdapter;
    @Delegate
    public EntityMetadataPacketAdapter entityMetadataPacketAdapter;
    @Delegate
    public PlayerRotationPacketAdapter playerRotationPacketAdapter = null;
    public BukkitAudiences adventure;

    public final Map<EntitySerializerInfo, EntityDataSerializer> entityDataSerializerMap = new HashMap<>();

    public void registerEntityDataSerializer(
            EntitySerializerInfo info,
            EntityDataSerializer entityDataSerializer
    ) {
        entityDataSerializerMap.put(info, entityDataSerializer);
    }

    public void registerEntityDataSerializer(
            @NotNull Class<?> serializerClass,
            @NotNull EntitySerializerInfo.Type serializerType,
            @NotNull EntityDataSerializer entityDataSerializer
    ) {
        entityDataSerializerMap.put(new EntitySerializerInfo(serializerClass, serializerType), entityDataSerializer);
    }

    /**
     * This method must only be used for unimplemented serializers or ones without an easy implementation.<br>
     * This is not to be kept final.
     */
    public void registerUnknownEntityDataSerializer(
            EntitySerializerInfo info,
            EntityDataSerializer entityDataSerializer
    ) {
        entityDataSerializerMap.put(info, entityDataSerializer);
    }

    public EntityDataSerializer getEntityDataSerializer(MetadataKey<?, ?> key) {
        EntityDataSerializer entityDataSerializer = entityDataSerializerMap.get(key.getEntitySerializerInfo());
        Preconditions.checkNotNull(entityDataSerializer, "Could not find EntityDataSerializer for " + key.getEntitySerializerInfo().serializerType() + " " + key.getObjectClass().getName());
        return entityDataSerializer;
    }
    public EntityDataSerializer getEntityDataSerializer(EntitySerializerInfo entitySerializerInfo) {
        EntityDataSerializer entityDataSerializer = entityDataSerializerMap.get(entitySerializerInfo);
        Preconditions.checkNotNull(entityDataSerializer, "Could not find EntityDataSerializer for " + entitySerializerInfo.serializerType() + " " + entitySerializerInfo.serializerClass().getName());
        return entityDataSerializer;
    }

    public EntityDataSerializer getEntityDataSerializer(Class<?> clazz, EntitySerializerInfo.Type type) {
        EntityDataSerializer entityDataSerializer = entityDataSerializerMap.get(new EntitySerializerInfo(clazz, type));
        Preconditions.checkNotNull(entityDataSerializer, "Could not find EntityDataSerializer for " + type + " " + clazz.getName());
        return entityDataSerializer;
    }

    public EntityDataSerializer getEntityDataSerializer(Class<?> clazz) {
        return entityDataSerializerMap.get(EntitySerializerInfo.normal(clazz));
    }

    public Class<?> getSerializableClass(Class<?> clazz) {
        if (Component.class.isAssignableFrom(clazz)) return getNMSComponentClass();
        else if (Flags.class.isAssignableFrom(clazz)) return Byte.class;
        else if (Pose.class.isAssignableFrom(clazz)) return getNMSPoseClass();
        else if (BlockVector.class.isAssignableFrom(clazz)) return getNMSBlockVectorClass();
        else if (Vector.class.isAssignableFrom(clazz)) return getNMSVectorClass();
        return clazz;
    }

    public void disable() {
        adventure.close();
        adventure = null;
    }

    public @Nullable Object asNMSObject(@Nullable Object object) {
        if (object instanceof NMSObject<?> nmsObject) {
            return nmsObject.asNMS();
        } else if (object instanceof net.kyori.adventure.text.Component component) {
            return asNMSComponent(component);
        } else if (object instanceof Pose pose) {
            return asNMSPose(pose);
        } else if (object instanceof BlockVector blockVector) {
            return asNMSBlockVector(blockVector);
        } else if (object instanceof Player player) {
            return asServerPlayer(player);
        }
        return object;
    }

    public void sendPacket(Player player, Object packet) {
        sendPacket(asServerPlayer(player), packet);
    }

}

