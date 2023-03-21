package me.cometkaizo.wavetech.lexer.tokens;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class Token {

    @NotNull
    public final TokenType type;
    @Nullable
    public final Object value;
    @NotNull
    public final Position start;
    @NotNull
    public final Position end;


    public Token(@NotNull TokenType type, @NotNull Position start, @NotNull Position end) {
        Objects.requireNonNull(type, "Type cannot be null");
        Objects.requireNonNull(start, "Start position cannot be null");
        Objects.requireNonNull(end, "End position cannot be null");
        this.type = type;
        this.value = null;
        this.start = start;
        this.end = end;
    }
    public Token(@NotNull TokenType type, @NotNull Object value, @NotNull Position start, @NotNull Position end) {
        Objects.requireNonNull(type, "Type cannot be null");
        Objects.requireNonNull(value, "Value cannot be null");
        Objects.requireNonNull(start, "Start position cannot be null");
        Objects.requireNonNull(end, "End position cannot be null");
        this.type = type;
        this.value = value;
        this.start = start;
        this.end = end;
    }

    @Nullable
    public Object getValue() {
        return value;
    }

    @NotNull
    public TokenType getType() {
        return type;
    }

    @Override
    public String toString() {
        if (value != null) {
            return getClass().getSimpleName() + "{" +
                    type.getClass().getSimpleName() + "." + type +
                    ", " + value +
                    '}';
        } else {
            return getClass().getSimpleName() + "{" +
                    type.getClass().getSimpleName() + "." + type +
                    '}';
        }
    }
}
