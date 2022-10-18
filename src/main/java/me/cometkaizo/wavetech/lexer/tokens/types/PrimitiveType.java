package me.cometkaizo.wavetech.lexer.tokens.types;

import me.cometkaizo.wavetech.lexer.tokens.TokenType;

public enum PrimitiveType implements TokenType {

    INT("int"),
    LONG("long"),
    SHORT("short"),
    BYTE("byte"),
    FLOAT("float"),
    DOUBLE("double"),
    BOOLEAN("boolean"),
    CHAR("char"),
    VOID("void");

    private final String symbol;

    PrimitiveType(String name) {
        this.symbol = name;
    }

    public String getSymbol() {
        return symbol;
    }

}
