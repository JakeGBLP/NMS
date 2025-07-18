package it.jakegblp.nms.impl.v1_21_3;

import it.jakegblp.nms.api.NMSAdapter;
import it.jakegblp.nms.api.entity.metadata.EntitySerializerInfo;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.Rotations;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.animal.CatVariant;
import net.minecraft.world.entity.animal.FrogVariant;
import net.minecraft.world.entity.animal.WolfVariant;
import net.minecraft.world.entity.animal.armadillo.Armadillo;
import net.minecraft.world.entity.animal.sniffer.Sniffer;
import net.minecraft.world.entity.decoration.PaintingVariant;
import net.minecraft.world.entity.npc.VillagerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.OptionalInt;
import java.util.UUID;

import static net.minecraft.network.syncher.EntityDataSerializers.*;

public final class v1_21_3 extends NMSAdapter<
        EntityDataSerializer<?>
        > {

    public v1_21_3() {
        super();
        registerEntityDataSerializer(EntitySerializerInfo.normal(Byte.class), BYTE);
        registerEntityDataSerializer(EntitySerializerInfo.normal(Integer.class), INT);
        registerEntityDataSerializer(EntitySerializerInfo.normal(Long.class), LONG);
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
        registerEntityDataSerializer(EntitySerializerInfo.normal(BlockState.class), BLOCK_STATE);
        registerEntityDataSerializer(EntitySerializerInfo.optional(BlockState.class), OPTIONAL_BLOCK_STATE);
        registerEntityDataSerializer(EntitySerializerInfo.normal(CompoundTag.class), COMPOUND_TAG);
        registerEntityDataSerializer(EntitySerializerInfo.normal(ParticleOptions.class), PARTICLE);
        registerEntityDataSerializer(EntitySerializerInfo.list(ParticleOptions.class), PARTICLES);
        registerEntityDataSerializer(EntitySerializerInfo.normal(VillagerData.class), VILLAGER_DATA);
        registerEntityDataSerializer(EntitySerializerInfo.normal(OptionalInt.class), OPTIONAL_UNSIGNED_INT);
        registerEntityDataSerializer(EntitySerializerInfo.normal(Pose.class), POSE);
        registerEntityDataSerializer(EntitySerializerInfo.holder(CatVariant.class), CAT_VARIANT);
        registerEntityDataSerializer(EntitySerializerInfo.holder(WolfVariant.class), WOLF_VARIANT);
        registerEntityDataSerializer(EntitySerializerInfo.holder(FrogVariant.class), FROG_VARIANT);
        registerEntityDataSerializer(EntitySerializerInfo.optional(GlobalPos.class), OPTIONAL_GLOBAL_POS);
        registerEntityDataSerializer(EntitySerializerInfo.holder(PaintingVariant.class), PAINTING_VARIANT);
        registerEntityDataSerializer(EntitySerializerInfo.normal(Sniffer.State.class), SNIFFER_STATE);
        registerEntityDataSerializer(EntitySerializerInfo.normal(Armadillo.ArmadilloState.class), ARMADILLO_STATE);
        registerEntityDataSerializer(EntitySerializerInfo.normal(Vector3f.class), VECTOR3);
        registerEntityDataSerializer(EntitySerializerInfo.normal(Quaternionf.class), QUATERNION);
    }



}