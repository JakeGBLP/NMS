package it.jakegblp.nms.skript.elements.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.bukkitutil.EntityUtils;
import ch.njol.skript.entity.EntityType;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.util.Direction;
import ch.njol.util.Kleenean;
import it.jakegblp.nms.api.NMSApi;
import it.jakegblp.nms.api.packets.client.EntitySpawnPacket;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class EffSpawnFakeEntity extends Effect {

    static {
        Skript.registerEffect(EffSpawnFakeEntity.class, "spawn [a] fake %entitytype% with id %number% %directions% %location% for %players%");
    }

    private Expression<EntityType> entityType;
    private Expression<Number> id;
    private Expression<Location> location;
    private Expression<Player> players;

    @Override
    protected void execute(Event event) {
        Location loc = location.getSingle(event);
        if (loc == null) return;
        EntityType type = entityType.getSingle(event);
        if (type == null) return;
        EntitySpawnPacket entitySpawnPacket = new EntitySpawnPacket(
                id.getOptionalSingle(event).orElse(NMSApi.generateRandomId()).intValue(),
                UUID.randomUUID(),
                loc.getX(),
                loc.getY(),
                loc.getZ(),
                loc.getPitch(),
                loc.getYaw(),
                EntityUtils.toBukkitEntityType(type.data),
                0,
                new Vector());
        for (Player player : players.getAll(event)) {
            NMSApi.sendPacket(player, entitySpawnPacket);
        }
    }

    @Override
    public String toString(@Nullable Event event, boolean b) {
        return "spawn fake " + entityType.toString(event, b) + " with id " + id.toString(event, b) + " " + location.toString(event, b);
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] expressions, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        entityType = (Expression<EntityType>) expressions[0];
        id = (Expression<Number>) expressions[1];
        location = Direction.combine((Expression<? extends Direction>) expressions[2], (Expression<? extends Location>) expressions[3]);
        players = (Expression<Player>) expressions[4];
        return true;
    }
}
