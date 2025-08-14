package it.jakegblp.nms.skript.utils;

import ch.njol.skript.lang.Expression;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

public class Utils {
    public static <T> T getExpressionValue(@Nullable Expression<T> expression, Event event, T defaultValue) {
        return expression == null ? defaultValue : expression.getOptionalSingle(event).orElse(defaultValue);
    }
}
