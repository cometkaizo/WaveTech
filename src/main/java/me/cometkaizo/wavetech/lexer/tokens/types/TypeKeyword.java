package me.cometkaizo.wavetech.lexer.tokens.types;

import me.cometkaizo.wavetech.lexer.tokens.TokenType;

public enum TypeKeyword implements TokenType {
    SYMBOL,
    SELF;

    @Override
    public String symbol() {
        return null;
    }

}
