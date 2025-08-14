package it.jakegblp.nms.skript.elements.expressions;

import ch.njol.skript.expressions.base.SimplePropertyExpression;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.Nullable;

public class ExprEntityId extends SimplePropertyExpression<Entity, Integer> {

    static {
        register(ExprEntityId.class, Integer.class, "entity id", "entities");
    }

    @Override
    public @Nullable Integer convert(Entity from) {
        return from.getEntityId();
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