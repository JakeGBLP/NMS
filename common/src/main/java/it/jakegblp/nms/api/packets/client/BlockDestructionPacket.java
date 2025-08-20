package it.jakegblp.nms.api.packets.client;

import lombok.*;
import org.bukkit.util.BlockVector;
import org.jetbrains.annotations.Range;

import static it.jakegblp.nms.api.AbstractNMS.NMS;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
public class BlockDestructionPacket implements ClientboundPacketWithId {
    protected int entityId;
    protected BlockVector position;
    protected @Range(from = -1, to = 9) int blockDestructionStage;

    public boolean causesRemoval() {
        return blockDestructionStage < 0;
    }

    @Override
    public Object asNMS() {
        return NMS.toNMSBlockDestructionPacket(this);
    }
}
