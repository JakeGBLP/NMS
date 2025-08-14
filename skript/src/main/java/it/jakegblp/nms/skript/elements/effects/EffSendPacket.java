package it.jakegblp.nms.skript.elements.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import it.jakegblp.nms.api.NMSApi;
import it.jakegblp.nms.api.packets.Packet;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

public class EffSendPacket extends Effect {

    static {
        Skript.registerEffect(EffSendPacket.class, "(send|dispatch) packet[s] %packets% to %players%");
    }

    private Expression<Packet> packetExpression;
    private Expression<Player> playerExpression;

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        packetExpression = (Expression<Packet>) expressions[0];
        playerExpression = (Expression<Player>) expressions[1];
        return true;
    }

    @Override
    protected void execute(Event event) {
        for (Packet packet : packetExpression.getAll(event)) {
            for (Player player : playerExpression.getAll(event)) {
                NMSApi.sendPacket(player, packet);
            }
        }
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "send packets " + packetExpression.toString(event, debug) + " to " + playerExpression.toString(event, debug);
    }
}
