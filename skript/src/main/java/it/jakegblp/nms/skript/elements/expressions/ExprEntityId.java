package it.jakegblp.nms.skript.elements.expressions;

import ch.njol.skript.expressions.base.SimplePropertyExpression;
import it.jakegblp.nms.api.packets.client.ClientboundPacketWithId;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.Nullable;

public class ExprEntityId extends SimplePropertyExpression<Object, Integer> {

    static {
        register(ExprEntityId.class, Integer.class, "entity id", "entities/packets");
    }

    @Override
    public @Nullable Integer convert(Object from) {
        if (from instanceof Entity entity)
            return entity.getEntityId();
        else if (from instanceof ClientboundPacketWithId packet)
            return packet.getEntityId();
        return null;
    }

    @Override
    protected String getPropertyName() {
        return "entity id";
    }

    @Override
    public Class<? extends Integer> getReturnType() {
        return Integer.class;
    }
}