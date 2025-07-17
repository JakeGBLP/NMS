package it.jakegblp.nms.impl.v1_17;

import it.jakegblp.nms.api.NMSAdapter;
import it.jakegblp.nms.api.entity.metadata.EntitySerializerInfo;
import net.minecraft.core.BlockPosition;
import net.minecraft.core.EnumDirection;
import net.minecraft.core.Vector3f;
import net.minecraft.core.particles.ParticleParam;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.network.syncher.DataWatcherSerializer;
import net.minecraft.world.entity.EntityPose;
import net.minecraft.world.entity.npc.VillagerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.IBlockData;

import java.util.OptionalInt;
import java.util.UUID;

import static net.minecraft.network.syncher.DataWatcherRegistry.*;

public final class v1_17 extends NMSAdapter<
        DataWatcherSerializer<?>
        > {

    public v1_17() {
        super();
        registerEntityDataSerializer(EntitySerializerInfo.normal(Byte.class), a);
        registerEntityDataSerializer(EntitySerializerInfo.normal(Integer.class), b);
        registerEntityDataSerializer(EntitySerializerInfo.normal(Float.class), c);
        registerEntityDataSerializer(EntitySerializerInfo.normal(String.class), d);
        registerEntityDataSerializer(EntitySerializerInfo.normal(IChatBaseComponent.class), e);
        registerEntityDataSerializer(EntitySerializerInfo.optional(IChatBaseComponent.class), f);
        registerEntityDataSerializer(EntitySerializerInfo.normal(ItemStack.class), g);
        registerEntityDataSerializer(EntitySerializerInfo.optional(IBlockData.class), h);
        registerEntityDataSerializer(EntitySerializerInfo.normal(Boolean.class), i);
        registerEntityDataSerializer(EntitySerializerInfo.normal(ParticleParam.class), j);
        registerEntityDataSerializer(EntitySerializerInfo.normal(Vector3f.class), k);
        registerEntityDataSerializer(EntitySerializerInfo.normal(BlockPosition.class), l);
        registerEntityDataSerializer(EntitySerializerInfo.optional(BlockPosition.class), m);
        registerEntityDataSerializer(EntitySerializerInfo.normal(EnumDirection.class), n);
        registerEntityDataSerializer(EntitySerializerInfo.optional(UUID.class), o);
        registerEntityDataSerializer(EntitySerializerInfo.normal(NBTTagCompound.class), p);
        registerEntityDataSerializer(EntitySerializerInfo.normal(VillagerData.class), q);
        registerEntityDataSerializer(EntitySerializerInfo.normal(OptionalInt.class), r);
        registerEntityDataSerializer(EntitySerializerInfo.normal(EntityPose.class), s);
    }



}