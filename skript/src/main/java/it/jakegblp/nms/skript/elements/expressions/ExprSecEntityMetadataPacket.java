package it.jakegblp.nms.skript.elements.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.config.SectionNode;
import ch.njol.skript.entity.EntityType;
import ch.njol.skript.expressions.base.SectionExpression;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.TriggerItem;
import ch.njol.util.Kleenean;
import it.jakegblp.nms.api.entity.metadata.EntityMetadata;
import it.jakegblp.nms.api.entity.metadata.MetadataKey;
import it.jakegblp.nms.api.packets.EntityMetadataPacket;
import it.jakegblp.nms.skript.api.DynamicEntryData;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.lang.entry.EntryContainer;
import org.skriptlang.skript.lang.entry.EntryValidator;
import org.skriptlang.skript.lang.entry.util.ExpressionEntryData;

import java.util.List;
import java.util.Map;

import static it.jakegblp.nms.skript.elements.expressions.ExprSecEntityMetadata.debug;

public class ExprSecEntityMetadataPacket extends SectionExpression<EntityMetadataPacket> {

    private static final EntryValidator VALIDATOR;

    static {
        VALIDATOR = EntryValidator.builder()
                .addEntryData(new ExpressionEntryData<>("id", null, false, Integer.class))
                .addEntryData(new DynamicEntryData("metadata", EntityMetadata.class, ExprSecEntityMetadata.VALIDATOR))
                .build();
        Skript.registerExpression(ExprSecEntityMetadataPacket.class, EntityMetadataPacket.class, ExpressionType.SIMPLE, "(an|a new|new) %entitytype% metadata packet");
    }

    private Expression<Number> idExpression;
    private Expression<EntityType> entityTypeExpression;
    private Map<MetadataKey.Named<? extends Entity, ?>, Expression<?>> expressionMap;
    private Expression<EntityMetadata> entityMetadataExpression;

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] expressions, int pattern, Kleenean delayed, SkriptParser.ParseResult result, @Nullable SectionNode node, @Nullable List<TriggerItem> triggerItems) {
        entityTypeExpression = (Expression<EntityType>) expressions[0];
        EntryContainer container = VALIDATOR.validate(node);
        if (container == null) return false;
        idExpression = (Expression<Number>) container.get("id", false);
        Object metadata = container.getOptional("metadata", false);
        if (metadata instanceof EntryContainer entryContainer) {
            expressionMap = ExprSecEntityMetadata.getMetadataExpressionMap(entityTypeExpression, entryContainer);
            debug(expressionMap, null);
        } else {
            entityMetadataExpression = (Expression<EntityMetadata>) metadata;
        }
        return true;
    }

    @Override
    protected EntityMetadataPacket[] get(Event event) {
        EntityType entityType = entityTypeExpression.getSingle(event);
        if (entityType == null) return new EntityMetadataPacket[0];
        Number id = idExpression.getSingle(event);
        if (id == null) return new EntityMetadataPacket[0];
        EntityMetadata metadata;
        if (expressionMap != null) {
            debug(expressionMap, event);
            metadata = ExprSecEntityMetadata.getMetadata(event, entityType.data.getType(), expressionMap);
        } else {
            metadata = entityMetadataExpression.getSingle(event);
        }
        if (metadata == null) return new EntityMetadataPacket[0];
        return new EntityMetadataPacket[]{new EntityMetadataPacket(
                id.intValue(),
                entityType.data.getType(),
                metadata
        )};
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends EntityMetadataPacket> getReturnType() {
        return EntityMetadataPacket.class;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "a new entity spawn packet";
    }
}
