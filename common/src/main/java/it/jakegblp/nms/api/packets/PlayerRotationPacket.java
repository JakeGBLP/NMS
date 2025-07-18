package it.jakegblp.nms.api.packets;

import it.jakegblp.nms.api.annotations.SinceMinecraft;
import it.jakegblp.nms.api.utils.Exceptionable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import static it.jakegblp.nms.api.NMSAdapter.NMS;

/**
 * <a href="https://minecraft.wiki/w/Java_Edition_protocol/Packets#Player_Rotation">Player Rotation Packet</a>
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = false)
@SinceMinecraft(version = "1.21.2")
public class PlayerRotationPacket extends ClientboundPacket implements Exceptionable<UnsupportedOperationException> {
    private float yaw, pitch;

    /**
     * @throws UnsupportedOperationException if the server version is below 1.21.2
     */
    public PlayerRotationPacket(float yaw, float pitch) {
        validate();
        this.yaw = yaw;
        this.pitch = pitch;
    }

    /**
     * @throws UnsupportedOperationException if the server version is below 1.21.2
     */
    @Override
    public Object asNMS() {
        validate();
        return NMS.playerRotationPacketAdapter.to(this);
    }

    /**
     * @throws UnsupportedOperationException if the server version is below 1.21.2
     */
    @Override
    public void validate() throws UnsupportedOperationException {
        if (NMS.playerRotationPacketAdapter == null)
            throw new UnsupportedOperationException("The 'Player Rotation' packet requires 1.21.2!");
    }
}
