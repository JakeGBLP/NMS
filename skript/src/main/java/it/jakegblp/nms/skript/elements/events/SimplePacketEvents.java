package it.jakegblp.nms.skript.elements.events;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.util.SimpleEvent;
import ch.njol.skript.registrations.EventValues;
import it.jakegblp.nms.api.events.PacketReceiveEvent;
import it.jakegblp.nms.api.events.PacketSendEvent;
import it.jakegblp.nms.api.packets.ClientboundPacket;
import it.jakegblp.nms.api.packets.ServerboundPacket;

public class SimplePacketEvents {
    static {
        Skript.registerEvent("Packet Receive", SimpleEvent.class, PacketReceiveEvent.class, "packet receive[d]")
                .description("Called when the server received a packet")
                .examples("on packet received:")
                .since("1.0.1");
        Skript.registerEvent("Packet Send", SimpleEvent.class, PacketSendEvent.class, "packet sen(d|t)")
                .description("Called when the server sends a packet")
                .examples("on packet sent:")
                .since("1.0.1");
        EventValues.registerEventValue(PacketReceiveEvent.class, ServerboundPacket.class, PacketReceiveEvent::getPacket, EventValues.TIME_NOW);
        EventValues.registerEventValue(PacketSendEvent.class, ClientboundPacket.class, PacketSendEvent::getPacket, EventValues.TIME_NOW);

    }
}
