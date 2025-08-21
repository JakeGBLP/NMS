package it.jakegblp.nms.impl.v1_18_2;

import com.github.zafarkhaja.semver.Version;
import it.jakegblp.nms.api.AbstractNMS;
import it.jakegblp.nms.api.adapters.*;
import it.jakegblp.nms.api.entity.metadata.EntitySerializerInfo;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Rotations;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.npc.VillagerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.OptionalInt;
import java.util.UUID;

import static net.minecraft.network.syncher.EntityDataSerializers.*;

public final class v1_18_2 extends AbstractNMS<
        EntityDataSerializer<?>
        > {

    public v1_18_2(JavaPlugin plugin, EntityTypeAdapter<?> entityTypeAdapter, MajorChangesAdapter majorChangesAdapter, ResourceLocationAdapter<?> resourceLocationAdapter, EntitySpawnPacketAdapter<?> entitySpawnPacketAdapter, EntityMetadataPacketAdapter<?> entityMetadataPacketAdapter, PlayerRotationPacketAdapter playerRotationPacketAdapter, ClientBundlePacketAdapter<?> clientBundlePacketAdapter, SetEquipmentPacketAdapter<?> setEquipmentPacketAdapter) {
        super(plugin, Version.of(1, 18, 2), entityTypeAdapter, majorChangesAdapter, resourceLocationAdapter, entitySpawnPacketAdapter, entityMetadataPacketAdapter, playerRotationPacketAdapter, clientBundlePacketAdapter, setEquipmentPacketAdapter);
    }

    @Override
    public void init() {
        registerEntityDataSerializer(EntitySerializerInfo.normal(Byte.class), BYTE);
        registerEntityDataSerializer(EntitySerializerInfo.normal(Integer.class), INT);
        registerEntityDataSerializer(EntitySerializerInfo.normal(Float.class), FLOAT);
        registerEntityDataSerializer(EntitySerializerInfo.normal(String.class), STRING);
        registerEntityDataSerializer(EntitySerializerInfo.normal(Component.class), COMPONENT);
        registerEntityDataSerializer(EntitySerializerInfo.optional(Component.class), OPTIONAL_COMPONENT);
        registerEntityDataSerializer(EntitySerializerInfo.normal(ItemStack.class), ITEM_STACK);
        registerEntityDataSerializer(EntitySerializerInfo.normal(Boolean.class), BOOLEAN);
        registerEntityDataSerializer(EntitySerializerInfo.normal(Rotations.class), ROTATIONS);
        registerEntityDataSerializer(EntitySerializerInfo.normal(BlockPos.class), BLOCK_POS);
        registerEntityDataSerializer(EntitySerializerInfo.optional(BlockPos.class), OPTIONAL_BLOCK_POS);
        registerEntityDataSerializer(EntitySerializerInfo.normal(Direction.class), DIRECTION);
        registerEntityDataSerializer(EntitySerializerInfo.optional(UUID.class), OPTIONAL_UUID);
        registerEntityDataSerializer(EntitySerializerInfo.optional(BlockState.class), BLOCK_STATE);
        registerEntityDataSerializer(EntitySerializerInfo.normal(CompoundTag.class), COMPOUND_TAG);
        registerEntityDataSerializer(EntitySerializerInfo.normal(ParticleOptions.class), PARTICLE);
        registerEntityDataSerializer(EntitySerializerInfo.normal(VillagerData.class), VILLAGER_DATA);
        registerEntityDataSerializer(EntitySerializerInfo.normal(OptionalInt.class), OPTIONAL_UNSIGNED_INT);
        registerEntityDataSerializer(EntitySerializerInfo.normal(Pose.class), POSE);
    }


}