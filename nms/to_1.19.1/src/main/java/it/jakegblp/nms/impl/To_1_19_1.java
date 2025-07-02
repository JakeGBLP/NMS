package it.jakegblp.nms.impl;

import it.jakegblp.nms.api.EntityMetadataPacketAdapter;
import it.jakegblp.nms.api.EntityTypeAdapter;
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

import static it.jakegblp.nms.api.NMSAdapter.nmsAdapter;
import static it.jakegblp.nms.api.entity.metadata.EntityDataSerializerInfo.Type.OPTIONAL;
import static it.jakegblp.nms.impl.SharedUtils.asNamespacedKey;
import static it.jakegblp.nms.impl.SharedUtils.asResourceLocation;

public class To_1_19_1 implements EntityTypeAdapter<EntityType<?>>, EntityMetadataPacketAdapter<ClientboundSetEntityDataPacket> {

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
    public ClientboundSetEntityDataPacket to(EntityMetadataPacket<?, ?> to) {
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
        synchedEntityData.assignValues((List<SynchedEntityData.DataItem<?>>) (List<?>) to.getEntityMetadata().getEntityDataMap().values().stream()
                .map(data -> new SynchedEntityData.DataItem<>(
                        ((EntityDataSerializer<Object>) nmsAdapter.getEntityDataSerializer(
                                data.getRawValueClass(),
                                data.getSerializerInfoType()
                        )).createAccessor(data.getIndex()),
                        data.getSerializerInfoType() == OPTIONAL ? data.getOptionalRawValue() : data.getRawValue()))
                .toList());
        return new ClientboundSetEntityDataPacket(to.getEntityId(), synchedEntityData, true);
    }
}
