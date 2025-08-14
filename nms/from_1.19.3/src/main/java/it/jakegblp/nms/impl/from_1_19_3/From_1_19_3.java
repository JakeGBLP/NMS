package it.jakegblp.nms.impl.from_1_19_3;

import it.jakegblp.nms.api.adapters.EntityMetadataPacketAdapter;
import it.jakegblp.nms.api.adapters.EntityTypeAdapter;
import it.jakegblp.nms.api.entity.metadata.EntitySerializerInfo;
import it.jakegblp.nms.api.entity.metadata.MetadataKey;
import it.jakegblp.nms.api.packets.EntityMetadataPacket;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import org.bukkit.Registry;
import org.bukkit.entity.Entity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static it.jakegblp.nms.api.AbstractNMS.NMS;
import static it.jakegblp.nms.api.entity.metadata.EntitySerializerInfo.Type.OPTIONAL;

public class From_1_19_3 implements
        EntityTypeAdapter<EntityType>, EntityMetadataPacketAdapter<ClientboundSetEntityDataPacket> {

    @Override
    public EntityType<?> toNMSEntityType(org.bukkit.entity.EntityType from) {
        return BuiltInRegistries.ENTITY_TYPE.getOptional((ResourceLocation) NMS.toNMSNamespacedKey(from.getKey())).orElseThrow();
    }

    @Override
    public org.bukkit.entity.EntityType fromNMSEntityType(EntityType to) {
        return Registry.ENTITY_TYPE.get(NMS.fromNMSNamespacedKey(BuiltInRegistries.ENTITY_TYPE.getKey(to)));
    }

    @Override
    public Class<EntityType> getNMSEntityTypeClass() {
        return EntityType.class;
    }

    @Override
    @SuppressWarnings("unchecked")
    public ClientboundSetEntityDataPacket toNMSEntityMetadataPacket(EntityMetadataPacket from) {
        // todo: turn most of this into an util method
        return new ClientboundSetEntityDataPacket(
                from.getEntityId(),
                (List<SynchedEntityData.DataValue<?>>) (List<?>) from.getEntityMetadata().metadataItems().entrySet().stream()
                        .filter(entry -> entry.getValue() != null)
                        .map(entry -> {
                            Object nmsValue = NMS.toNMSObject(entry.getValue());
                            MetadataKey<?, ?> key = entry.getKey();
                            EntitySerializerInfo.Type type = key.getEntitySerializerInfo().serializerType();
                            return new SynchedEntityData.DataValue<>(
                                    key.getIndex(),
                                    (EntityDataSerializer<Object>) NMS.getEntityDataSerializer(
                                            NMS.getSerializableClass(key.getObjectClass()), type),
                                    type == OPTIONAL ? Optional.ofNullable(nmsValue) : nmsValue);
                        }).toList()
        );
    }

    @Override
    public EntityMetadataPacket fromNMSEntityMetadataPacket(ClientboundSetEntityDataPacket from) {
        Map<MetadataKey<? extends Entity, ?>, Object> metadata = new HashMap<>();
        List<SynchedEntityData.DataValue<?>> dataValueList = from.packedItems();
        for (SynchedEntityData.DataValue<?> dataValue : dataValueList) {
            Object value = dataValue.value();
            metadata.put(new MetadataKey<>(Entity.class, dataValue.id(), value), value);
        }
        return new EntityMetadataPacket(from.id(), metadata);
    }

    @Override
    public Class<ClientboundSetEntityDataPacket> getNMSEntityMetadataPacketClass() {
        return ClientboundSetEntityDataPacket.class;
    }
}