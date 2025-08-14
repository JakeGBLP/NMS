package it.jakegblp.nms.skript.elements.types;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.registrations.Classes;
import it.jakegblp.nms.api.entity.metadata.EntityMetadata;
import it.jakegblp.nms.api.packets.*;
import it.jakegblp.nms.skript.api.EnumWrapper;
import org.bukkit.entity.Pose;

public class Types {
    static {
        Classes.registerClass(new ClassInfo<>(Packet.class, "packet")
                .user("packets?")
                .name("Packet")
                .description("A packet.") // add example
                .since("1.0.1"));
        Classes.registerClass(new ClassInfo<>(ClientboundPacket.class, "clientboundpacket")
                .user("client ?bound ?packets?")
                .name("Clientbound Packet")
                .description("An packet bound to the client. Sent from the server to the client.") // add example
                .since("1.0.1"));
        Classes.registerClass(new ClassInfo<>(ServerboundPacket.class, "serverboundpacket")
                .user("server ?bound ?packets?")
                .name("Serverbound Packet")
                .description("An packet bound to the server. Sent from the client to the server.") // add example
                .since("1.0.1"));
        Classes.registerClass(new ClassInfo<>(EntitySpawnPacket.class, "entityspawnpacket")
                .user("entity spawn? packets?")
                .name("Entity Spawn Packet")
                .description("An entity spawn packet.") // add example
                .since("1.0.1"));
        Classes.registerClass(new ClassInfo<>(EntityMetadataPacket.class, "entitymetadatapacket")
                .user("entity metadata? packets?")
                .name("Entity Metadata Packet")
                .description("An entity metadata packet.") // add example
                .since("1.0.1"));
        Classes.registerClass(new ClassInfo<>(EntityMetadata.class, "entitymetadata")
                .user("entity ?metadatas?")
                .name("Entity Metadata")
                .description("An entity's metadata") // add example
                .since("1.0.1"));


        // Bukkit
        if (Skript.classExists("org.bukkit.entity.Pose") && Classes.getExactClassInfo(Pose.class) == null) {
            EnumWrapper<Pose> POSE_ENUM = new EnumWrapper<>(Pose.class, null, "pose");
            Classes.registerClass(POSE_ENUM.getClassInfo("pose")
                    .user("poses?")
                    .name("Entity - Pose")
                    .description("All the Poses.") // add example
                    .since("1.0.1"));
        }
    }
}
