package me.cometkaizo.wavetech.lexer.tokens.types;

import me.cometkaizo.wavetech.lexer.tokens.TokenType;

public enum ModifierKeyword implements TokenType {

    STATIC("static"),
    FINAL("final"),
    ABSTRACT("abstract"),
    SEALED("sealed"),
    UNSEALED("unsealed"),
    VOLATILE("volatile"),
    TRANSIENT("transient"),
    NATIVE("native");

    public final String symbol;

    ModifierKeyword(String symbol) {
        this.symbol = symbol;
    }

    @Override
    public String symbol() {
        return symbol;
    }
}
