package it.jakegblp.nms.impl;

import it.jakegblp.nms.api.EntityMetadataPacketAdapter;
import it.jakegblp.nms.api.EntityTypeAdapter;
import it.jakegblp.nms.api.entity.metadata.EntitySerializerInfo;
import it.jakegblp.nms.api.entity.metadata.key.MetadataKey;
import it.jakegblp.nms.api.packets.EntityMetadataPacket;
import it.jakegblp.nms.api.utils.SharedUtils;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import org.bukkit.Registry;

import java.util.List;
import java.util.Optional;

import static it.jakegblp.nms.api.NMSAdapter.NMS;
import static it.jakegblp.nms.api.entity.metadata.EntitySerializerInfo.Type.OPTIONAL;
import static it.jakegblp.nms.api.utils.SharedUtils.asNMS;

public class From_1_19_3 implements EntityTypeAdapter, EntityMetadataPacketAdapter {

    @Override
    public EntityType<?> from(org.bukkit.entity.EntityType from) {
        return BuiltInRegistries.ENTITY_TYPE.getOptional(SharedUtils.asResourceLocation(from.getKey())).orElseThrow();
    }

    @Override
    public org.bukkit.entity.EntityType to(EntityType<?> to) {
        return Registry.ENTITY_TYPE.get(SharedUtils.asNamespacedKey(BuiltInRegistries.ENTITY_TYPE.getKey(to)));
    }

    @Override
    @SuppressWarnings("unchecked")
    public ClientboundSetEntityDataPacket to(EntityMetadataPacket<?, ?> from) {
        // todo: turn most of this into an util method
        return new ClientboundSetEntityDataPacket(
                from.getEntityId(),
                (List<SynchedEntityData.DataValue<?>>) (List<?>) from.getEntityMetadata().getMetadataItems().entrySet().stream()
                        .filter(entry -> entry.getValue() != null)
                        .map(entry -> {
                            Object nmsValue = asNMS(entry.getValue());
                            MetadataKey<?, ?> key = entry.getKey();
                            EntitySerializerInfo.Type type = key.getEntitySerializerInfo().serializerType();
                            return new SynchedEntityData.DataValue<>(
                                    key.getIndex(),
                                    (EntityDataSerializer<Object>) NMS.getEntityDataSerializer(
                                            NMS.getSerializableClass(nmsValue.getClass()),
                                            type
                                    ),
                                    type == OPTIONAL ? Optional.of(nmsValue) : nmsValue);
                        }).toList()
        );
    }
}