package it.jakegblp.nms.impl;

import it.jakegblp.nms.api.EntityMetadataPacketAdapter;
import it.jakegblp.nms.api.EntityTypeAdapter;
import it.jakegblp.nms.api.entity.metadata.EntitySerializerInfo;
import it.jakegblp.nms.api.entity.metadata.key.MetadataKey;
import it.jakegblp.nms.api.packets.EntityMetadataPacket;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static it.jakegblp.nms.api.NMSAdapter.NMS;
import static it.jakegblp.nms.api.entity.metadata.EntitySerializerInfo.Type.OPTIONAL;
import static it.jakegblp.nms.api.utils.SharedUtils.*;

public class To_1_19_1 implements EntityTypeAdapter, EntityMetadataPacketAdapter {

    @Override
    public EntityType<?> from(org.bukkit.entity.EntityType from) {
        return Registry.ENTITY_TYPE.get(asResourceLocation(from.getKey()));
    }

    @Override
    public org.bukkit.entity.EntityType to(EntityType<?> to) {
        return org.bukkit.Registry.ENTITY_TYPE.get(asNamespacedKey(Registry.ENTITY_TYPE.getKey(to)));
    }

    @Override
    @SuppressWarnings("unchecked")
    public ClientboundSetEntityDataPacket to(EntityMetadataPacket<?, ?> from) {
        SynchedEntityData synchedEntityData = new SynchedEntityData(new Entity(null, null) {
                    @Override
                    protected void defineSynchedData() {

                    }

                    @Override
                    protected void readAdditionalSaveData(CompoundTag compoundTag) {

                    }

                    @Override
                    protected void addAdditionalSaveData(CompoundTag compoundTag) {

                    }

                    @Override
                    public Packet<?> getAddEntityPacket() {
                        return null;
                    }
                });
        synchedEntityData.assignValues((List<SynchedEntityData.DataItem<?>>) (List<?>) from.getEntityMetadata()
                .getMetadataItems()
                .entrySet()
                .stream()
                .filter(Objects::nonNull)
                .map(entry -> {
                    Object nmsValue = asNMS(entry.getValue());
                    MetadataKey<?, ?> key = entry.getKey();
                    EntitySerializerInfo.Type type = key.getEntitySerializerInfo().serializerType();
                    return new SynchedEntityData.DataItem<>(
                            ((EntityDataSerializer<Object>) NMS.getEntityDataSerializer(
                                    NMS.getSerializableClass(nmsValue.getClass()),
                                    type
                            )).createAccessor(key.getIndex()),
                            type == OPTIONAL ? Optional.of(nmsValue) : nmsValue);
                }).toList());
        return new ClientboundSetEntityDataPacket(from.getEntityId(), synchedEntityData, true);
    }
}
