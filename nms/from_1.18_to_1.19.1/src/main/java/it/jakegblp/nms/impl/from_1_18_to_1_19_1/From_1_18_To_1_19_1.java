package it.jakegblp.nms.impl.from_1_18_to_1_19_1;

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
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static it.jakegblp.nms.api.NMSAdapter.NMS;
import static it.jakegblp.nms.api.entity.metadata.EntitySerializerInfo.Type.OPTIONAL;

public class From_1_18_To_1_19_1 implements
        EntityTypeAdapter<EntityType<?>>, EntityMetadataPacketAdapter<ClientboundSetEntityDataPacket> {

    @Override
    public EntityType<?> toNMSEntityType(org.bukkit.entity.EntityType from) {
        return Registry.ENTITY_TYPE.get((ResourceLocation) NMS.asResourceLocation(from.getKey()));
    }

    @Override
    public org.bukkit.entity.EntityType toEntityType(EntityType<?> to) {
        return org.bukkit.Registry.ENTITY_TYPE.get(NMS.asNamespacedKey(Registry.ENTITY_TYPE.getKey(to)));
    }

    @Override
    @SuppressWarnings("unchecked")
    public ClientboundSetEntityDataPacket toNMSEntityMetadataPacket(EntityMetadataPacket<?, ?> from) {
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
                    Object nmsValue = NMS.asNMSObject(entry.getValue());
                    MetadataKey<?, ?> key = entry.getKey();
                    EntitySerializerInfo.Type type = key.getEntitySerializerInfo().serializerType();
                    return new SynchedEntityData.DataItem<>(
                            ((EntityDataSerializer<Object>) NMS.getEntityDataSerializer(
                                    NMS.getSerializableClass(key.getObjectClass()), type)
                            ).createAccessor(key.getIndex()),
                            type == OPTIONAL ? Optional.ofNullable(nmsValue) : nmsValue);
                }).toList());
        return new ClientboundSetEntityDataPacket(from.getEntityId(), synchedEntityData, true);
    }
}
