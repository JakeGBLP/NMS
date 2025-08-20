package it.jakegblp.nms.api;

import com.google.common.base.Preconditions;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import it.jakegblp.nms.api.adapters.*;
import it.jakegblp.nms.api.entity.metadata.EntitySerializerInfo;
import it.jakegblp.nms.api.entity.metadata.Flags;
import it.jakegblp.nms.api.entity.metadata.MetadataKey;
import it.jakegblp.nms.api.listeners.InjectionListener;
import it.jakegblp.nms.api.packets.Packet;
import it.jakegblp.nms.api.utils.NMSObject;
import lombok.Getter;
import lombok.experimental.Delegate;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.entity.Pose;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.BlockVector;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractNMS<
        EntityDataSerializer
        > {

    public static AbstractNMS<?> NMS;
    public final BiMap<EntitySerializerInfo, EntityDataSerializer> entityDataSerializerMap = HashBiMap.create();
    @Delegate
    public final EntityTypeAdapter entityTypeAdapter;
    @Delegate
    public final MajorChangesAdapter majorChangesAdapter;
    @Delegate
    public final ResourceLocationAdapter resourceLocationAdapter;
    @Delegate
    public final EntitySpawnPacketAdapter entitySpawnPacketAdapter;
    @Delegate
    public final EntityMetadataPacketAdapter entityMetadataPacketAdapter;
    @Delegate
    public final PlayerRotationPacketAdapter playerRotationPacketAdapter;
    @Delegate
    public final ClientBundlePacketAdapter clientBundlePacketAdapter;
    @Delegate
    public final SetEquipmentPacketAdapter setEquipmentPacketAdapter;
    @Getter
    private JavaPlugin plugin;


    public AbstractNMS(
            JavaPlugin plugin,
            EntityTypeAdapter<?> entityTypeAdapter,
            MajorChangesAdapter majorChangesAdapter,
            ResourceLocationAdapter<?> resourceLocationAdapter,
            EntitySpawnPacketAdapter<?> entitySpawnPacketAdapter,
            EntityMetadataPacketAdapter<?> entityMetadataPacketAdapter,
            PlayerRotationPacketAdapter playerRotationPacketAdapter,
            ClientBundlePacketAdapter<?> clientBundlePacketAdapter,
            SetEquipmentPacketAdapter<?> setEquipmentPacketAdapter
    ) {
        this.plugin = plugin;
        this.entityTypeAdapter = entityTypeAdapter;
        this.majorChangesAdapter = majorChangesAdapter;
        this.resourceLocationAdapter = resourceLocationAdapter;
        this.entitySpawnPacketAdapter = entitySpawnPacketAdapter;
        this.entityMetadataPacketAdapter = entityMetadataPacketAdapter;
        this.playerRotationPacketAdapter = playerRotationPacketAdapter;
        this.clientBundlePacketAdapter = clientBundlePacketAdapter;
        this.setEquipmentPacketAdapter = setEquipmentPacketAdapter;
        Bukkit.getPluginManager().registerEvents(new InjectionListener(), plugin);
        init();
    }

    public abstract void init();

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

    @SuppressWarnings("unchecked")
    public EntitySerializerInfo getEntityDataSerializerInfo(Object serializer) {
        EntitySerializerInfo entityDataSerializerInfo = entityDataSerializerMap.inverse().get((EntityDataSerializer) serializer);
        Preconditions.checkNotNull(entityDataSerializerInfo, "Could not find entityDataSerializerInfo for Serializer " + serializer);
        return entityDataSerializerInfo;
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

    public @Nullable Object toNMSObject(@Nullable Object object) {
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

    /**
     * This method takes an NMS Packet and returns a non NMS Packet
     *
     * @param object an NMS Packet
     * @return the packet converted into its library counterpart
     */
    public @Nullable Packet fromNMSPacket(@Nullable Object object) {
        if (object == null) return null;
        else if (isNMSSetEquipmentPacket(object)) return fromNMSSetEquipmentPacket(object);
        else if (isNMSBlockDestructionPacket(object)) return fromNMSBlockDestructionPacket(object);
        else if (isNMSClientBundlePacket(object)) return fromNMSClientBundlePacket(object);
        else if (isNMSEntityMetadataPacket(object)) return fromNMSEntityMetadataPacket(object);
        else if (isNMSEntitySpawnPacket(object)) return fromNMSEntitySpawnPacket(object);
        else if (isNMSPlayerRotationPacket(object)) return fromNMSPlayerRotationPacket(object);
        return null;
    }

    public @Nullable Object fromNMSObject(@Nullable Object object) {
        if (object == null) return null;
        else if (isNMSNamespacedKey(object)) return fromNMSNamespacedKey(object);
        else if (isNMSEntityType(object)) return fromNMSEntityType(object);
        else if (isNMSPacket(object)) return fromNMSPacket(object);
        return null;
    }

}

