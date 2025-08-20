package it.jakegblp.nms.skript.api;

import ch.njol.skript.classes.Parser;
import ch.njol.skript.lang.ParseContext;
import org.jetbrains.annotations.NotNull;


public abstract class SimpleParser<T> extends Parser<T> {

    private final boolean canParse;

    public SimpleParser(boolean canParse) {
        this.canParse = canParse;
    }

    @Override
    public boolean canParse(ParseContext context) {
        return canParse;
    }

    @Override
    public @NotNull String toString(T t, int flags) {
        return toString(t);
    }

    @Override
    public @NotNull String toVariableNameString(T t) {
        return toString(t, 0);
    }

    public abstract @NotNull String toString(T t);
}
