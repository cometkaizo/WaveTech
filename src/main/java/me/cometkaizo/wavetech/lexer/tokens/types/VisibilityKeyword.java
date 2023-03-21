package me.cometkaizo.wavetech.lexer.tokens.types;

import me.cometkaizo.wavetech.lexer.tokens.TokenType;

public enum VisibilityKeyword implements TokenType {
    PUBLIC("public"),
    PROTECTED("protected"),
    PACKAGE_PRIVATE(null),
    PRIVATE("private");

    private final String symbol;

    VisibilityKeyword(String symbol) {
        this.symbol = symbol;
    }

    @Override
    public String symbol() {
        return symbol;
    }
}
