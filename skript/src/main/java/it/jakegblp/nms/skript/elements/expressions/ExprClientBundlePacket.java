package it.jakegblp.nms.skript.elements.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.config.SectionNode;
import ch.njol.skript.expressions.base.SectionExpression;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.TriggerItem;
import ch.njol.util.Kleenean;
import it.jakegblp.nms.api.packets.client.ClientBundlePacket;
import it.jakegblp.nms.api.packets.client.ClientboundPacket;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class ExprClientBundlePacket extends SectionExpression<ClientBundlePacket> {

    @Getter
    @AllArgsConstructor
    public static class BundleEvent extends Event {
        private Collection<ClientboundPacket> packets;

        @Override
        public @NotNull HandlerList getHandlers() {
            throw new IllegalStateException();
        }
    }

    static {
        Skript.registerExpression(ExprClientBundlePacket.class, ClientBundlePacket.class, ExpressionType.COMBINED,
                "[a] new client bundle [delimiter] packet");
    }

    private List<Expression<? extends ClientboundPacket>> packetExpressionList = null;

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] expressions, int pattern, Kleenean delayed, SkriptParser.ParseResult result, @Nullable SectionNode node, @Nullable List<TriggerItem> triggerItems) {
        if (node == null)
            Skript.warning("Empty bundle packet.");
        else {
            node.iterator().forEachRemaining(subNode -> {
                String key = subNode.getKey();
                if (key != null) {
                    if (packetExpressionList == null) packetExpressionList = new ArrayList<>();
                    packetExpressionList.add(new SkriptParser(key).parseExpression(ClientboundPacket.class));
                }
            });
        }
        return true;
    }

    @Override
    protected ClientBundlePacket @Nullable [] get(Event event) {
        if (packetExpressionList == null) return new ClientBundlePacket[0];
        return new ClientBundlePacket[] {new ClientBundlePacket(packetExpressionList.stream().filter(Objects::nonNull).map(expression -> expression.getSingle(event)).filter(Objects::nonNull).toList())};
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends ClientBundlePacket> getReturnType() {
        return ClientBundlePacket.class;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "a new client bundle packet";
    }
}
