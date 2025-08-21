package it.jakegblp.nms.impl.v1_21_6;

import com.github.zafarkhaja.semver.Version;
import it.jakegblp.nms.api.AbstractNMS;
import it.jakegblp.nms.api.adapters.*;
import it.jakegblp.nms.api.entity.metadata.EntitySerializerInfo;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.Rotations;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.world.entity.EntityReference;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.animal.CatVariant;
import net.minecraft.world.entity.animal.ChickenVariant;
import net.minecraft.world.entity.animal.CowVariant;
import net.minecraft.world.entity.animal.PigVariant;
import net.minecraft.world.entity.animal.armadillo.Armadillo;
import net.minecraft.world.entity.animal.sniffer.Sniffer;
import net.minecraft.world.entity.animal.wolf.WolfSoundVariant;
import net.minecraft.world.entity.decoration.PaintingVariant;
import net.minecraft.world.entity.npc.VillagerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.bukkit.plugin.java.JavaPlugin;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.OptionalInt;

import static net.minecraft.network.syncher.EntityDataSerializers.*;

public final class v1_21_6 extends AbstractNMS<
        EntityDataSerializer<?>
        > {

    public v1_21_6(JavaPlugin plugin, EntityTypeAdapter<?> entityTypeAdapter, MajorChangesAdapter majorChangesAdapter, ResourceLocationAdapter<?> resourceLocationAdapter, EntitySpawnPacketAdapter<?> entitySpawnPacketAdapter, EntityMetadataPacketAdapter<?> entityMetadataPacketAdapter, PlayerRotationPacketAdapter playerRotationPacketAdapter, ClientBundlePacketAdapter<?> clientBundlePacketAdapter, SetEquipmentPacketAdapter<?> setEquipmentPacketAdapter) {
        super(plugin, Version.of(1, 21, 6), entityTypeAdapter, majorChangesAdapter, resourceLocationAdapter, entitySpawnPacketAdapter, entityMetadataPacketAdapter, playerRotationPacketAdapter, clientBundlePacketAdapter, setEquipmentPacketAdapter);
    }

    @Override
    public void init() {
        registerEntityDataSerializer(EntitySerializerInfo.normal(Byte.class), BYTE);
        registerEntityDataSerializer(EntitySerializerInfo.normal(Integer.class), INT);
        registerEntityDataSerializer(EntitySerializerInfo.normal(Long.class), LONG);
        registerEntityDataSerializer(EntitySerializerInfo.normal(Float.class), FLOAT);
        registerEntityDataSerializer(EntitySerializerInfo.normal(String.class), STRING);
        registerEntityDataSerializer(EntitySerializerInfo.normal(Component.class), COMPONENT);
        registerEntityDataSerializer(EntitySerializerInfo.optional(Component.class), OPTIONAL_COMPONENT);
        registerEntityDataSerializer(EntitySerializerInfo.normal(ItemStack.class), ITEM_STACK);
        registerEntityDataSerializer(EntitySerializerInfo.normal(BlockState.class), BLOCK_STATE);
        registerEntityDataSerializer(EntitySerializerInfo.normal(Boolean.class), BOOLEAN);
        registerEntityDataSerializer(EntitySerializerInfo.normal(Rotations.class), ROTATIONS);
        registerEntityDataSerializer(EntitySerializerInfo.normal(BlockPos.class), BLOCK_POS);
        registerEntityDataSerializer(EntitySerializerInfo.optional(BlockPos.class), OPTIONAL_BLOCK_POS);
        registerEntityDataSerializer(EntitySerializerInfo.normal(Direction.class), DIRECTION);
        // todo: handle
        registerUnknownEntityDataSerializer(EntitySerializerInfo.optional(EntityReference.class), OPTIONAL_LIVING_ENTITY_REFERENCE);
        registerEntityDataSerializer(EntitySerializerInfo.optional(GlobalPos.class), OPTIONAL_GLOBAL_POS);
        registerEntityDataSerializer(EntitySerializerInfo.optional(BlockState.class), OPTIONAL_BLOCK_STATE);
        registerEntityDataSerializer(EntitySerializerInfo.normal(CompoundTag.class), COMPOUND_TAG);
        registerEntityDataSerializer(EntitySerializerInfo.normal(ParticleOptions.class), PARTICLE);
        registerEntityDataSerializer(EntitySerializerInfo.list(ParticleOptions.class), PARTICLES);
        registerEntityDataSerializer(EntitySerializerInfo.normal(VillagerData.class), VILLAGER_DATA);
        registerEntityDataSerializer(EntitySerializerInfo.normal(OptionalInt.class), OPTIONAL_UNSIGNED_INT);
        registerEntityDataSerializer(EntitySerializerInfo.normal(Pose.class), POSE);
        registerEntityDataSerializer(EntitySerializerInfo.holder(CatVariant.class), CAT_VARIANT);
        registerEntityDataSerializer(EntitySerializerInfo.holder(ChickenVariant.class), CHICKEN_VARIANT);
        registerEntityDataSerializer(EntitySerializerInfo.holder(CowVariant.class), COW_VARIANT);
        registerEntityDataSerializer(EntitySerializerInfo.holder(net.minecraft.world.entity.animal.wolf.WolfVariant.class), WOLF_VARIANT);
        registerEntityDataSerializer(EntitySerializerInfo.holder(WolfSoundVariant.class), WOLF_SOUND_VARIANT);
        registerEntityDataSerializer(EntitySerializerInfo.holder(net.minecraft.world.entity.animal.frog.FrogVariant.class), FROG_VARIANT);
        registerEntityDataSerializer(EntitySerializerInfo.holder(PigVariant.class), PIG_VARIANT);
        registerEntityDataSerializer(EntitySerializerInfo.holder(PaintingVariant.class), PAINTING_VARIANT);
        registerEntityDataSerializer(EntitySerializerInfo.normal(Sniffer.State.class), SNIFFER_STATE);
        registerEntityDataSerializer(EntitySerializerInfo.normal(Armadillo.ArmadilloState.class), ARMADILLO_STATE);
        registerEntityDataSerializer(EntitySerializerInfo.normal(Vector3f.class), VECTOR3);
        registerEntityDataSerializer(EntitySerializerInfo.normal(Quaternionf.class), QUATERNION);
    }

}
