package it.jakegblp.nms.impl;

import it.jakegblp.nms.api.EntityMetadataPacketAdapter;
import it.jakegblp.nms.api.EntityTypeAdapter;
import it.jakegblp.nms.api.packets.EntityMetadataPacket;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import org.bukkit.Registry;

import java.util.List;

import static it.jakegblp.nms.api.NMSAdapter.nmsAdapter;
import static it.jakegblp.nms.api.entity.metadata.EntityDataSerializerInfo.Type.OPTIONAL;

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
                (List<SynchedEntityData.DataValue<?>>) (List<?>) to.getEntityMetadata().getEntityDataMap().values().stream()
                        .map(data -> new SynchedEntityData.DataValue<>(
                                data.getIndex(),
                                (EntityDataSerializer<Object>) nmsAdapter.getEntityDataSerializer(
                                        data.getRawValueClass(),
                                        data.getSerializerInfoType()
                                ),
                                data.getSerializerInfoType() == OPTIONAL ? data.getOptionalRawValue() : data.getRawValue())).toList()
        );
    }
}