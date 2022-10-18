package me.cometkaizo.wavetech.lexer.tokens.types;

import me.cometkaizo.wavetech.lexer.tokens.TokenType;

public enum VisibilityModifier implements TokenType {
    PUBLIC("public"),
    PROTECTED("protected"),
    PACKAGE_PRIVATE(""),
    PRIVATE("private");

    private final String symbol;

    VisibilityModifier(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }
}
