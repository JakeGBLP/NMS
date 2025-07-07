package it.jakegblp.nms.api;

import com.google.common.base.Preconditions;
import it.jakegblp.nms.api.entity.metadata.EntitySerializerInfo;
import it.jakegblp.nms.api.entity.metadata.Flags;
import it.jakegblp.nms.api.entity.metadata.key.MetadataKey;
import it.jakegblp.nms.api.packets.EntityMetadataPacket;
import it.jakegblp.nms.api.packets.EntitySpawnPacket;
import it.jakegblp.nms.api.utils.SharedUtils;
import lombok.Getter;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static it.jakegblp.nms.api.utils.ReflectionUtils.getDeclaredConstructor;
import static it.jakegblp.nms.api.utils.ReflectionUtils.newInstance;
import static it.jakegblp.nms.api.utils.SharedUtils.asNMS;

public abstract class NMSAdapter {

    public static NMSAdapter NMS;

    public JavaPlugin javaPlugin;
    public EntityTypeAdapter entityTypeAdapter;
    public EntitySpawnPacketAdapter entitySpawnPacketAdapter;
    public EntityMetadataPacketAdapter entityMetadataPacketAdapter;
    public PlayerRotationPacketAdapter playerRotationPacketAdapter = null;
    public BukkitAudiences adventure;

    public final Map<EntitySerializerInfo<?>, EntityDataSerializer<?>> entityDataSerializerMap = new HashMap<>();

    @NotNull
    public final Class<? extends GenericNMSServerPlayer> NMS_SERVER_PLAYER_CLASS;
    @NotNull
    public final Constructor<? extends GenericNMSServerPlayer> NMS_SERVER_PLAYER_CONSTRUCTOR;

    @SuppressWarnings("unchecked")
    public NMSAdapter() {
        Class<? extends GenericNMSServerPlayer> nmsServerPlayerClass = null;
        Constructor<? extends GenericNMSServerPlayer> nmsServerPlayerConstructor = null;
        for (Class<?> declaredClass : getClass().getDeclaredClasses()) {
            if (GenericNMSServerPlayer.class.isAssignableFrom(declaredClass)) {
                nmsServerPlayerClass = (Class<? extends GenericNMSServerPlayer>) declaredClass;
                nmsServerPlayerConstructor = (Constructor<? extends GenericNMSServerPlayer>) getDeclaredConstructor(
                        nmsServerPlayerClass,
                        getClass(),
                        Player.class
                );
            }
        }
        Preconditions.checkArgument(nmsServerPlayerClass != null, "Could not find ServerPlayer class.");
        NMS_SERVER_PLAYER_CLASS = nmsServerPlayerClass;
        Preconditions.checkArgument(nmsServerPlayerConstructor != null, "Could not find ServerPlayer constructor.");
        NMS_SERVER_PLAYER_CONSTRUCTOR = nmsServerPlayerConstructor;
    }

    public <T> void registerEntityDataSerializer(
            EntitySerializerInfo<T> info,
            EntityDataSerializer<T> entityDataSerializer
    ) {
        entityDataSerializerMap.put(info, entityDataSerializer);
    }
    public <T> void registerOptionalEntityDataSerializer(
            EntitySerializerInfo<T> info,
            EntityDataSerializer<Optional<T>> entityDataSerializer
    ) {
        entityDataSerializerMap.put(info, entityDataSerializer);
    }
    public <T> void registerHolderEntityDataSerializer(
            EntitySerializerInfo<T> info,
            EntityDataSerializer<Holder<T>> entityDataSerializer
    ) {
        entityDataSerializerMap.put(info, entityDataSerializer);
    }
    public <T> void registerListEntityDataSerializer(
            EntitySerializerInfo<T> info,
            EntityDataSerializer<List<T>> entityDataSerializer
    ) {
        entityDataSerializerMap.put(info, entityDataSerializer);
    }

    /**
     * This method must only be used for unimplemented serializers or ones without an easy implementation.<br>
     * This is not to be kept final.
     */
    public void registerUnknownEntityDataSerializer(
            EntitySerializerInfo<?> info,
            EntityDataSerializer<?> entityDataSerializer
    ) {
        entityDataSerializerMap.put(info, entityDataSerializer);
    }

    @SuppressWarnings("unchecked")
    public <T> EntityDataSerializer<T> getEntityDataSerializer(MetadataKey<?, T> key) {
        return (EntityDataSerializer<T>) entityDataSerializerMap.get(new EntitySerializerInfo<>(key.getEntityClass(), key.getEntitySerializerInfo().serializerType()));
    }

    @SuppressWarnings("unchecked")
    public <T> EntityDataSerializer<T> getEntityDataSerializer(Class<T> clazz, EntitySerializerInfo.Type type) {
        EntityDataSerializer<T> entityDataSerializer = (EntityDataSerializer<T>) entityDataSerializerMap.get(new EntitySerializerInfo<>(clazz, type));
        Preconditions.checkNotNull(entityDataSerializer, "Could not find EntityDataSerializer for " + type + " " + clazz.getName());
        return entityDataSerializer;
    }

    @SuppressWarnings("unchecked")
    public <T> EntityDataSerializer<T> getEntityDataSerializer(Class<T> clazz) {
        return (EntityDataSerializer<T>) entityDataSerializerMap.get(EntitySerializerInfo.normal(clazz));
    }

    public GenericNMSServerPlayer asGenericNMSServerPlayer(Player player) {
        return (GenericNMSServerPlayer) newInstance(NMS_SERVER_PLAYER_CONSTRUCTOR, this, player);
    }

    public Class<?> getSerializableClass(Class<?> clazz) {
        if (Component.class.isAssignableFrom(clazz)) return Component.class;
        else if (Flags.class.isAssignableFrom(clazz)) return Byte.class;
        return clazz;
    }

    @Getter
    public abstract class GenericNMSServerPlayer {

        protected final ServerPlayer serverPlayer;
        protected final Player player;

        public GenericNMSServerPlayer(Player player) {
            this.serverPlayer = (ServerPlayer) asNMS(player);
            this.player = player;
        }

        protected void sendPacket(Packet<?> packet) {
            SharedUtils.sendPacket(player, packet);
        }

        public void sendEntitySpawnPacket(EntitySpawnPacket packet) {
            sendPacket(entitySpawnPacketAdapter.to(packet));
        }
        public void sendEntityMetadataPacket(EntityMetadataPacket<?, ?> packet) {
            sendPacket(entityMetadataPacketAdapter.to(packet));
        }

    }

    public void disable() {
        adventure.close();
        adventure = null;
    }
}

