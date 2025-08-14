package it.jakegblp.nms.api.events;

import it.jakegblp.nms.api.packets.ClientboundPacket;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

@Getter
@Setter
public class PacketSendEvent extends PacketEvent<ClientboundPacket> implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();
    protected boolean cancelled;

    public PacketSendEvent(ClientboundPacket packet) {
        super(packet);
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

}
