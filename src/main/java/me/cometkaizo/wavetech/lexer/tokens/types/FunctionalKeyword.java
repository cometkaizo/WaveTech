package me.cometkaizo.wavetech.lexer.tokens.types;

import me.cometkaizo.wavetech.lexer.tokens.TokenType;

public enum FunctionalKeyword implements TokenType {

    IF("if"),
    ELSE("else"),
    SWITCH("switch"),
    CASE("case"),
    YIELD("yield"),
    DEFAULT("default"),
    DO("do"),
    WHILE("while"),
    FOR("for"),
    RETURN("return"),
    BREAK("break"),
    CONTINUE("continue"),
    TRY("try"),
    CATCH("catch"),
    FINALLY("finally"),
    INSTANCE_OF("instanceof"),
    NEW("new"),
    SYNCHRONIZED("synchronized");

    private final String symbol;

    FunctionalKeyword(String symbol) {
        if (symbol != null && symbol.isBlank()) throw new IllegalArgumentException("Cannot create blank functional keyword '" + this + "' : '" + symbol + "'");
        this.symbol = symbol;
    }

    @Override
    public String symbol() {
        return symbol;
    }
}
