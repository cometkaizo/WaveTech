package me.cometkaizo.wavetech.lexer.tokens.types;

import me.cometkaizo.wavetech.lexer.tokens.TokenType;

public enum PrimitiveValue implements TokenType {


    INT("int"),
    LONG("long"),
    SHORT("short"),
    BYTE("byte"),
    FLOAT("float"),
    DOUBLE("double"),
    BOOLEAN("boolean"),
    CHAR("char"),
    NULL("null");


    private final String symbol;

    PrimitiveValue(String symbol) {
        this.symbol = symbol;
    }

    @Override
    public String getSymbol() {
        return symbol;
    }
}
