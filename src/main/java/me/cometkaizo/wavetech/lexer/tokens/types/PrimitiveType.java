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

    private final String[] symbols;

    PrimitiveType(String... symbols) {
        this.symbols = symbols;
    }

    @Override
    public String[] symbolSeq() {
        return symbols;
    }

}
