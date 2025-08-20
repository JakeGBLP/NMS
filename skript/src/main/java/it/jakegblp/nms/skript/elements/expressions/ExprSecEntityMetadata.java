package it.jakegblp.nms.skript.elements.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.config.SectionNode;
import ch.njol.skript.entity.EntityType;
import ch.njol.skript.expressions.base.SectionExpression;
import ch.njol.skript.lang.*;
import ch.njol.util.Kleenean;
import it.jakegblp.nms.api.entity.metadata.EntityMetadata;
import it.jakegblp.nms.api.entity.metadata.MetadataKey.Named;
import it.jakegblp.nms.api.entity.metadata.MetadataKeyRegistries;
import it.jakegblp.nms.skript.api.SimpleEntryValidator;
import lombok.Setter;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.lang.entry.EntryContainer;
import org.skriptlang.skript.lang.entry.EntryValidator;

import java.util.*;

@Setter
public class ExprSecEntityMetadata extends SectionExpression<EntityMetadata> {

    public static final EntryValidator VALIDATOR;

    static {
        SimpleEntryValidator builder = SimpleEntryValidator.builder();
        MetadataKeyRegistries.getRegistry().forEach((entityClass, namedMetadataKeyList) -> {
            for (Named<?, ?> metadataKey : namedMetadataKeyList) {
                builder.addOptionalEntry(metadataKey.getName().toLowerCase(Locale.ROOT).replace("_", " "), metadataKey.getObjectClass());
            }
        });
        VALIDATOR = builder.build();
        Skript.registerExpression(ExprSecEntityMetadata.class, EntityMetadata.class, ExpressionType.SIMPLE,
                "[a] new %entitytype% metadata [object]");
    }

    private Expression<EntityType> entityTypeExpression;
    private Map<Named<? extends Entity, ?>, Expression<?>> expressionMap;

    public static Map<Named<? extends Entity, ?>, Expression<?>> getMetadataExpressionMap(
            Expression<EntityType> entityTypeExpression,
            EntryContainer container
    ) {
        Map<Named<? extends Entity, ?>, Expression<?>> expressionMap = new HashMap<>();
        List<String> unsupportedProperties = null;

        Class<? extends Entity> type = null;
        EntityType entityType = null;
        if (entityTypeExpression instanceof Literal<EntityType> entityTypeLiteral) {
            entityType = entityTypeLiteral.getSingle();
            if (entityType == null) {
                return null;
            }
            type = entityType.data.getType();
            if (type == null) {
                return null;
            }
        }

        for (var entry : MetadataKeyRegistries.getRegistry().entrySet()) {
            Class<?> supportedClass = entry.getKey();
            var namedMetadataKeyList = entry.getValue();

            for (Named<?, ?> namedMetadataKey : namedMetadataKeyList) {
                String normalizedName = namedMetadataKey.getName().toLowerCase().replace("_", " ");

                if (container.hasEntry(normalizedName)) {
                    if (type == null || supportedClass.isAssignableFrom(type)) {
                        if (unsupportedProperties == null || unsupportedProperties.isEmpty())
                            expressionMap.put(namedMetadataKey, (Expression<?>) container.getOptional(normalizedName, false));
                    } else {
                        if (unsupportedProperties == null) unsupportedProperties = new ArrayList<>();
                        unsupportedProperties.add(namedMetadataKey.getName());
                    }
                }
            }
        }
        if (unsupportedProperties != null && !unsupportedProperties.isEmpty()) {
            String errorMessage = entityType + " does not support the following properties: " +
                    String.join(", ", unsupportedProperties);
            Skript.error(errorMessage);
            return null;
        }

        return expressionMap;
    }

    public static EntityMetadata getMetadata(Event event, Class<? extends Entity> type, Map<Named<? extends Entity, ?>, Expression<?>> expressionMap) {
        Map<Named<? extends Entity, ?>, Object> values = new HashMap<>();
        for (var entry : expressionMap.entrySet()) {
            Named<? extends Entity, ?> namedMetadataKey = entry.getKey();
            Expression<?> expr = entry.getValue();
            if (namedMetadataKey.getEntityClass().isAssignableFrom(type)) {
                Object value = expr.getSingle(event);
                if (value != null)
                    values.put(namedMetadataKey, value);
            }
        }
        return new EntityMetadata(values);
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] expressions, int pattern, Kleenean delayed, SkriptParser.ParseResult result, @Nullable SectionNode node, @Nullable List<TriggerItem> triggerItems) {
        EntryContainer container = VALIDATOR.validate(node);
        if (container == null) return false;
        entityTypeExpression = (Expression<EntityType>) expressions[0];
        expressionMap = getMetadataExpressionMap(entityTypeExpression, container);
        return expressionMap != null;
    }

    @Override
    protected EntityMetadata[] get(Event event) {
        EntityType entityType = entityTypeExpression.getSingle(event);
        if (entityType == null) return new EntityMetadata[0];
        Class<? extends Entity> type = entityType.data.getType();
        if (type == null) return new EntityMetadata[0];
        return new EntityMetadata[]{getMetadata(event, type, expressionMap)};
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends EntityMetadata> getReturnType() {
        return EntityMetadata.class;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "a new " + entityTypeExpression.toString(event, debug) + " metadata";
    }
}
