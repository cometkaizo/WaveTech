package me.cometkaizo.wavetech.lexer.tokens.types;

import me.cometkaizo.wavetech.lexer.tokens.TokenType;
import org.jetbrains.annotations.Nullable;

public enum ReferenceKeyword implements TokenType {
    THIS("this"),
    SUPER("super");

    public final String symbol;
    ReferenceKeyword(String symbol) {
        this.symbol = symbol;
    }

    @Override
    public @Nullable String symbol() {
        return symbol;
    }
}
