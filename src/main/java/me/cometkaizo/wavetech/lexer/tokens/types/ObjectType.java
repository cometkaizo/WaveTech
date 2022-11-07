package me.cometkaizo.wavetech.lexer.tokens.types;

import me.cometkaizo.wavetech.lexer.tokens.TokenType;

public enum ObjectType implements TokenType {
    SYMBOL("%symbol%"),
    SYMBOL_OR_REFERENCE("%symbol-ref%"),
    REFERENCE("%reference%");

    private final String[] symbol = new String[1];

    ObjectType(String name) {
        this.symbol[0] = name;
    }

    @Override
    public String[] symbolSeq() {
        return symbol;
    }

}
