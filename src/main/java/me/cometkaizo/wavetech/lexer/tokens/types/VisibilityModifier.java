package me.cometkaizo.wavetech.lexer.tokens.types;

import me.cometkaizo.wavetech.lexer.tokens.TokenType;

public enum VisibilityModifier implements TokenType {
    PUBLIC(3, "public"),
    PROTECTED(2, "protected"),
    PACKAGE_PRIVATE(1, ""),
    PRIVATE(0, "private");

    private final String[] symbols;
    private final int power;

    VisibilityModifier(int power, String... symbols) {
        this.symbols = symbols;
        this.power = power;
    }

    @Override
    public String[] symbolSeq() {
        return symbols;
    }

    public int power() {
        return power;
    }
}
