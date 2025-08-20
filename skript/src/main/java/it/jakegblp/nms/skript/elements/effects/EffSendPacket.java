package it.jakegblp.nms.skript.elements.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import it.jakegblp.nms.api.packets.client.ClientboundPacket;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

public class EffSendPacket extends Effect {

    static {
        Skript.registerEffect(EffSendPacket.class, "(send|dispatch) packet[s] %clientboundpackets% to %players%");
    }

    private Expression<ClientboundPacket> clientboundPacketExpression;
    private Expression<Player> playerExpression;

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        clientboundPacketExpression = (Expression<ClientboundPacket>) expressions[0];
        playerExpression = (Expression<Player>) expressions[1];
        return true;
    }

    @Override
    protected void execute(Event event) {
        for (ClientboundPacket packet : clientboundPacketExpression.getAll(event))
            for (Player player : playerExpression.getAll(event))
                packet.send(player);
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "send packets " + clientboundPacketExpression.toString(event, debug) + " to " + playerExpression.toString(event, debug);
    }
}
