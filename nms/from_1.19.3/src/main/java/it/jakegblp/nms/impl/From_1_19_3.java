package it.jakegblp.nms.impl;

import it.jakegblp.nms.api.EntityMetadataPacketAdapter;
import it.jakegblp.nms.api.EntityTypeAdapter;
import it.jakegblp.nms.api.entity.metadata.keys.MetadataKey;
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

import static it.jakegblp.nms.api.NMSAdapter.nmsAdapter;
import static it.jakegblp.nms.api.entity.metadata.EntityDataSerializerInfo.Type.OPTIONAL;
import static it.jakegblp.nms.api.utils.SharedUtils.asNMS;

public class From_1_19_3 implements EntityTypeAdapter<EntityType<?>>, EntityMetadataPacketAdapter<ClientboundSetEntityDataPacket> {

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
    public ClientboundSetEntityDataPacket to(EntityMetadataPacket<?, ?> to) {
        return new ClientboundSetEntityDataPacket(
                to.getEntityId(),
                (List<SynchedEntityData.DataValue<?>>) (List<?>) to.getEntityMetadata().getMetadataItems().entrySet().stream()
                        .filter(entry -> entry.getValue() != null)
                        .map(entry -> {
                            Object nmsValue = asNMS(entry.getValue());
                            MetadataKey<?, ?> key = entry.getKey();
                            return new SynchedEntityData.DataValue<>(
                                    key.getIndex(),
                                    (EntityDataSerializer<Object>) nmsAdapter.getEntityDataSerializer(
                                            SharedUtils.getAsSerializableType(nmsValue.getClass()),
                                            key.getSerializationType()
                                    ),
                                    key.getSerializationType() == OPTIONAL ? Optional.of(nmsValue) : nmsValue);
                        }).toList()
        );
    }
}