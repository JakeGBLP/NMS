package it.jakegblp.nms.api.packets;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.bukkit.util.BlockVector;
import org.jetbrains.annotations.Range;

import static it.jakegblp.nms.api.AbstractNMS.NMS;

@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class BlockDestructionPacket extends ClientboundPacketWithId {

    protected BlockVector position;
    protected @Range(from = 0, to = 9) int blockDestructionStage;

    public BlockDestructionPacket(int entityId, BlockVector blockVector, @Range(from = 0, to = 9) int blockDestructionStage) {
        super(entityId);
        this.position = blockVector;
        this.blockDestructionStage = blockDestructionStage;
    }

    @Override
    public Object asNMS() {
        return NMS.toNMSBlockDestructionPacket(this);
    }
}
