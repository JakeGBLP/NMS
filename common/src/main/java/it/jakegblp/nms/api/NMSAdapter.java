package it.jakegblp.nms.api;

import it.jakegblp.nms.api.entity.metadata.EntityDataSerializerInfo;
import it.jakegblp.nms.api.packets.EntityMetadataPacket;
import it.jakegblp.nms.api.packets.EntitySpawnPacket;
import it.jakegblp.nms.api.utils.ReflectionUtils;
import it.jakegblp.nms.impl.SharedUtils;
import lombok.Getter;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

import static it.jakegblp.nms.api.utils.ReflectionUtils.getDeclaredConstructor;
import static it.jakegblp.nms.api.utils.ReflectionUtils.newInstance;
import static it.jakegblp.nms.impl.SharedUtils.asNMS;

@Getter
public abstract class NMSAdapter<
        NMSServerPlayer,
        NMSEntityDataSerializer,
        NMSPacket,
        NMSEntitySpawnPacket extends NMSPacket,
        NMSEntityMetadataPacket extends NMSPacket
        > {

    public static NMSAdapter nmsAdapter;

    public EntityTypeAdapter<?> entityTypeAdapter;
    public EntitySpawnPacketAdapter<?> entitySpawnPacketAdapter;
    public EntityMetadataPacketAdapter<?> entityMetadataPacketAdapter;
    public BukkitAudiences adventure;

    public final Map<EntityDataSerializerInfo<?>, NMSEntityDataSerializer> entityDataSerializerMap = new HashMap<>();

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
        if (nmsServerPlayerClass == null)
            throw new RuntimeException("Could not find NMSServerPlayer class");
        NMS_SERVER_PLAYER_CLASS = nmsServerPlayerClass;
        if (nmsServerPlayerConstructor == null)
            throw new RuntimeException("Could not find NMSServerPlayer constructor");
        NMS_SERVER_PLAYER_CONSTRUCTOR = nmsServerPlayerConstructor;
    }

    public Class<?> getNMSComponentClass() {
        return ReflectionUtils.getClass("net.minecraft.network.chat.IChatBaseComponent");
    }

    public Class<?> getNMSPoseClass() {
        return ReflectionUtils.getClass("net.minecraft.world.entity.EntityPose");
    }

    public Class<?> getNMSBlockVectorClass() {
        return ReflectionUtils.getClass("net.minecraft.core.BlockPosition");
    }

    public <T> NMSEntityDataSerializer getEntityDataSerializer(Class<T> clazz, EntityDataSerializerInfo.Type type) {
        return entityDataSerializerMap.get(new EntityDataSerializerInfo<>(clazz, type));
    }

    public NMSEntityDataSerializer getEntityDataSerializer(@NotNull Class<?> clazz) {
        return entityDataSerializerMap.get(new EntityDataSerializerInfo<>(clazz));
    }

    @SuppressWarnings("unchecked")
    public GenericNMSServerPlayer asGenericNMSServerPlayer(Player player) {
        return (GenericNMSServerPlayer) newInstance(NMS_SERVER_PLAYER_CONSTRUCTOR, this, player);
    }

    public void printMetadataPacket(NMSEntityMetadataPacket packet) {
    }
    public void printSpawnPacket(NMSEntitySpawnPacket packet) {
    }

    @Getter
    public abstract class GenericNMSServerPlayer {

        protected final NMSServerPlayer serverPlayer;
        protected final Player player;

        @SuppressWarnings("unchecked")
        public GenericNMSServerPlayer(Player player) {
            serverPlayer = (NMSServerPlayer) asNMS(player);
            this.player = player;
        }

        protected void sendPacket(NMSPacket packet) {
            SharedUtils.sendPacket(player, packet);
        }

        @SuppressWarnings("unchecked")
        public void sendEntitySpawnPacket(EntitySpawnPacket packet) {
            sendPacket((NMSPacket) entitySpawnPacketAdapter.to(packet));
        }
        @SuppressWarnings("unchecked")
        public void sendEntityMetadataPacket(EntityMetadataPacket<?, ?> packet) {
            sendPacket((NMSPacket) entityMetadataPacketAdapter.to(packet));
        }

    }
}

