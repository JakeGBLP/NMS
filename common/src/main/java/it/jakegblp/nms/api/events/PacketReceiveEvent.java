package it.jakegblp.nms.api.events;

import it.jakegblp.nms.api.packets.ServerboundPacket;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

@Getter
@Setter
public class PacketReceiveEvent extends PacketEvent<ServerboundPacket> implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();
    protected boolean cancelled;

    public PacketReceiveEvent(ServerboundPacket packet) {
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
