package me.cometkaizo.wavetech.lexer.tokens.types;

import me.cometkaizo.wavetech.lexer.tokens.TokenType;

public enum DeclarationKeyword implements TokenType {

    CLASS("class"),
    INTERFACE("interface"),
    ANNOTATION("@interface"),
    ENUM("enum"),
    RECORD("record");

    private final String[] symbols;

    DeclarationKeyword(String... symbols) {
        this.symbols = symbols;
    }

    public String[] symbolSeq() {
        return symbols;
    }
}
