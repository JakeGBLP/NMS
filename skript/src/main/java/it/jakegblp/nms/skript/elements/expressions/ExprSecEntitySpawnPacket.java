package it.jakegblp.nms.skript.elements.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.bukkitutil.EntityUtils;
import ch.njol.skript.config.SectionNode;
import ch.njol.skript.entity.EntityType;
import ch.njol.skript.expressions.base.SectionExpression;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.TriggerItem;
import ch.njol.util.Kleenean;
import it.jakegblp.nms.api.NMSApi;
import it.jakegblp.nms.api.packets.client.EntitySpawnPacket;
import it.jakegblp.nms.skript.api.SimpleEntryValidator;
import org.bukkit.Location;
import org.bukkit.event.Event;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.lang.entry.EntryContainer;
import org.skriptlang.skript.lang.entry.EntryValidator;

import java.util.List;
import java.util.UUID;

import static it.jakegblp.nms.skript.utils.Utils.getExpressionValue;

public class ExprSecEntitySpawnPacket extends SectionExpression<EntitySpawnPacket> {

    private static final EntryValidator VALIDATOR;

    static {
        VALIDATOR = SimpleEntryValidator.builder()
                .addOptionalEntry("id", Integer.class)
                .addOptionalEntry("uuid", UUID.class)
                .addRequiredEntry("location", Location.class)
                .addRequiredEntry("entity type", EntityType.class)
                .addOptionalEntry("velocity", Vector.class)
                .addOptionalEntry("data", Integer.class)
                .addOptionalEntry("head yaw", Number.class)
                .build();
        Skript.registerExpression(ExprSecEntitySpawnPacket.class, EntitySpawnPacket.class, ExpressionType.SIMPLE,
                "[a] new entity spawn packet");
    }

    private Expression<Integer> entityId;
    private Expression<UUID> entityUUID;
    private Expression<Location> location;
    private Expression<EntityType> entityType;
    private Expression<Vector> velocity;
    private Expression<Number> data;
    private Expression<Number> headYaw;

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] expressions, int pattern, Kleenean delayed, SkriptParser.ParseResult result, @Nullable SectionNode node, @Nullable List<TriggerItem> triggerItems) {
        EntryContainer container = VALIDATOR.validate(node);
        if (container == null) return false;
        entityId = (Expression<Integer>) container.getOptional("id", false);
        entityUUID = (Expression<UUID>) container.getOptional("uuid", false);
        location = (Expression<Location>) container.get("location", false);
        entityType = (Expression<EntityType>) container.get("entity type", false);
        velocity = (Expression<Vector>) container.getOptional("velocity", false);
        data = (Expression<Number>) container.getOptional("data", false);
        headYaw = (Expression<Number>) container.getOptional("head yaw", false);
        return true;
    }

    @Override
    protected EntitySpawnPacket[] get(Event event) {
        Location loc = location.getSingle(event);
        if (loc == null) return new EntitySpawnPacket[0];
        EntityType type = entityType.getSingle(event);
        if (type == null) return new EntitySpawnPacket[0];
        return new EntitySpawnPacket[]{new EntitySpawnPacket(
                getExpressionValue(entityId, event, NMSApi.generateRandomId()),
                getExpressionValue(entityUUID, event, UUID.randomUUID()),
                loc.getX(),
                loc.getY(),
                loc.getZ(),
                loc.getPitch(),
                loc.getYaw(),
                EntityUtils.toBukkitEntityType(type.data),
                getExpressionValue(data, event, 0).intValue(),
                getExpressionValue(velocity, event, new Vector()),
                getExpressionValue(headYaw, event, 0).doubleValue()
        )};
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends EntitySpawnPacket> getReturnType() {
        return EntitySpawnPacket.class;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "a new entity spawn packet";
    }
}
