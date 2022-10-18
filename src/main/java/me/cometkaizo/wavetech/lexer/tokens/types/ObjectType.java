package me.cometkaizo.wavetech.lexer.tokens.types;

import me.cometkaizo.wavetech.lexer.tokens.TokenType;

public enum ObjectType implements TokenType {
    SYMBOL("%symbol%"),
    SYMBOL_OR_REFERENCE("%symbol-ref%"),
    REFERENCE("%reference%");

    private final String symbol;

    ObjectType(String name) {
        this.symbol = name;
    }

    public String getSymbol() {
        return symbol;
    }

}
