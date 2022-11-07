package me.cometkaizo.wavetech.lexer.tokens;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class Token {

    @NotNull
    protected final TokenType type;
    @Nullable
    protected final Object value;


    public Token(@NotNull TokenType type) {
        Objects.requireNonNull(type, "Type cannot be null");
        this.type = type;
        value = null;
    }
    public Token(@NotNull TokenType type, @NotNull Object value) {
        Objects.requireNonNull(type, "Type cannot be null");
        Objects.requireNonNull(value, "Value cannot be null");
        this.type = type;
        this.value = value;
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
