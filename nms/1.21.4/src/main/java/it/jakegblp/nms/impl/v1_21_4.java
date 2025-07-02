package it.jakegblp.nms.impl;

import it.jakegblp.nms.api.NMSAdapter;
import it.jakegblp.nms.api.entity.metadata.EntityDataSerializerInfo;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.Rotations;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.server.level.ServerPlayer;
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

import static it.jakegblp.nms.api.entity.metadata.EntityDataSerializerInfo.Type.*;
import static net.minecraft.network.syncher.EntityDataSerializers.*;

public final class v1_21_4 extends NMSAdapter<
        ServerPlayer,
        EntityDataSerializer<?>,
        Packet<?>,
        ClientboundAddEntityPacket,
        ClientboundSetEntityDataPacket
        > {

    public v1_21_4() {
        entityDataSerializerMap.put(new EntityDataSerializerInfo<>(Byte.class), BYTE);
        entityDataSerializerMap.put(new EntityDataSerializerInfo<>(Integer.class), INT);
        entityDataSerializerMap.put(new EntityDataSerializerInfo<>(Long.class), LONG);
        entityDataSerializerMap.put(new EntityDataSerializerInfo<>(Float.class), FLOAT);
        entityDataSerializerMap.put(new EntityDataSerializerInfo<>(String.class), STRING);
        entityDataSerializerMap.put(new EntityDataSerializerInfo<>(Component.class), COMPONENT);
        entityDataSerializerMap.put(new EntityDataSerializerInfo<>(Component.class, OPTIONAL), OPTIONAL_COMPONENT);
        entityDataSerializerMap.put(new EntityDataSerializerInfo<>(ItemStack.class), ITEM_STACK);
        entityDataSerializerMap.put(new EntityDataSerializerInfo<>(BlockState.class), BLOCK_STATE);
        entityDataSerializerMap.put(new EntityDataSerializerInfo<>(BlockState.class, OPTIONAL), OPTIONAL_BLOCK_STATE);
        entityDataSerializerMap.put(new EntityDataSerializerInfo<>(Boolean.class), BOOLEAN);
        entityDataSerializerMap.put(new EntityDataSerializerInfo<>(ParticleOptions.class), PARTICLE);
        entityDataSerializerMap.put(new EntityDataSerializerInfo<>(ParticleOptions.class, LIST), PARTICLES);
        entityDataSerializerMap.put(new EntityDataSerializerInfo<>(Rotations.class), ROTATIONS);
        entityDataSerializerMap.put(new EntityDataSerializerInfo<>(BlockPos.class), BLOCK_POS);
        entityDataSerializerMap.put(new EntityDataSerializerInfo<>(BlockPos.class, OPTIONAL), OPTIONAL_BLOCK_POS);
        entityDataSerializerMap.put(new EntityDataSerializerInfo<>(Direction.class), DIRECTION);
        entityDataSerializerMap.put(new EntityDataSerializerInfo<>(UUID.class, OPTIONAL), OPTIONAL_UUID);
        entityDataSerializerMap.put(new EntityDataSerializerInfo<>(GlobalPos.class, OPTIONAL), OPTIONAL_GLOBAL_POS);
        entityDataSerializerMap.put(new EntityDataSerializerInfo<>(CompoundTag.class), COMPOUND_TAG);
        entityDataSerializerMap.put(new EntityDataSerializerInfo<>(VillagerData.class), VILLAGER_DATA);
        entityDataSerializerMap.put(new EntityDataSerializerInfo<>(OptionalInt.class), OPTIONAL_UNSIGNED_INT);
        entityDataSerializerMap.put(new EntityDataSerializerInfo<>(Pose.class), POSE);
        entityDataSerializerMap.put(new EntityDataSerializerInfo<>(CatVariant.class, HOLDER), CAT_VARIANT);
        entityDataSerializerMap.put(new EntityDataSerializerInfo<>(WolfVariant.class, HOLDER), WOLF_VARIANT);
        entityDataSerializerMap.put(new EntityDataSerializerInfo<>(FrogVariant.class, HOLDER), FROG_VARIANT);
        entityDataSerializerMap.put(new EntityDataSerializerInfo<>(PaintingVariant.class, HOLDER), PAINTING_VARIANT);
        entityDataSerializerMap.put(new EntityDataSerializerInfo<>(Armadillo.ArmadilloState.class), ARMADILLO_STATE);
        entityDataSerializerMap.put(new EntityDataSerializerInfo<>(Sniffer.State.class), SNIFFER_STATE);
        entityDataSerializerMap.put(new EntityDataSerializerInfo<>(Vector3f.class), VECTOR3);
        entityDataSerializerMap.put(new EntityDataSerializerInfo<>(Quaternionf.class), QUATERNION);
    }
    /*
    @Override
    public void printMetadataPacket(ClientboundSetEntityDataPacket clientboundSetEntityDataPacket) {
        System.out.println("=== Metadata Packet Dump ===");
        System.out.println("Entity ID: " + clientboundSetEntityDataPacket.id());

        List<SynchedEntityData.DataValue<?>> metadata = clientboundSetEntityDataPacket.packedItems();
        for (SynchedEntityData.DataValue<?> dataValue : metadata) {
            int index = dataValue.id();
            Object value = dataValue.value();
            System.out.printf("Index %d: %s (%s)%n", index,
                    value,
                    value != null ? value.getClass().getSimpleName() : "null");
        }

        System.out.println("=== End of Dump ===");
    }

    @Override
    public void printSpawnPacket(ClientboundAddEntityPacket packet) {
        System.out.println("=== Spawn Packet Dump ===");

        System.out.println("Entity ID: " + packet.getId());
        System.out.println("Entity UUID: " + packet.getUUID());

        EntityType<?> entityType = packet.getType();
        System.out.println("Entity Type: " + entityType + " (" + entityType.getDescriptionId() + ")");

        System.out.println("X: " + packet.getX());
        System.out.println("Y: " + packet.getY());
        System.out.println("Z: " + packet.getZ());

        System.out.println("Pitch: " + packet.getXRot());
        System.out.println("Yaw: " + packet.getYRot());
        System.out.println("Head Yaw: " + packet.getYHeadRot());

        System.out.println("Velocity X: " + packet.getXa());
        System.out.println("Velocity Y: " + packet.getYa());
        System.out.println("Velocity Z: " + packet.getZa());

        System.out.println("Entity Data (custom): " + packet.getData());

        System.out.println("=== End of Dump ===");
    }
     */

    public class NMSServerPlayer extends GenericNMSServerPlayer {

        public NMSServerPlayer(org.bukkit.entity.Player player) {
            super(player);
        }
        /*
        @Override
        public void testSendEntityMetadataPacket(int id) {
            byte flags = 0x01 | 0x40; // ON_FIRE (0x01) + GLOWING (0x40)
            SynchedEntityData.DataValue<Byte> flagValue = new SynchedEntityData.DataValue<>(0, EntityDataSerializers.BYTE, flags);
            ClientboundSetEntityDataPacket clientboundSetEntityDataPacket = new ClientboundSetEntityDataPacket(id, List.of(flagValue));
            printMetadataPacket(clientboundSetEntityDataPacket);
            sendPacket(clientboundSetEntityDataPacket);
        }

         */
    }

}
