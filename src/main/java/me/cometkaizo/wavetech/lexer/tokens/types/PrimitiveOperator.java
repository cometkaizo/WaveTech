package me.cometkaizo.wavetech.lexer.tokens.types;

import me.cometkaizo.wavetech.lexer.tokens.TokenType;

public enum PrimitiveOperator implements TokenType {

    DOT("."),
    EQUALS("="),
    DOUBLE_EQUALS("=="),
    PLUS("+"),
    INCREMENT("++"),
    PLUS_ASSIGN("+="),
    MINUS("-"),
    DECREMENT("--"),
    MINUS_ASSIGN("-="),
    ARROW("->"),
    STRONG_ARROW("=>"),
    LESS_THAN("<"),
    GREATER_THAN(">"),
    LESS_THAN_OR_EQUAL("<="),
    GREATER_THAN_OR_EQUAL(">="),
    EXCLAMATION_MARK("!"),
    ASPERAND("@"),
    PERCENT("%"),
    CARET("^"),
    AMPERSAND("&"),
    DAMPERSAND("&&"),
    PIPE("|"),
    DOUBLE_PIPE("||"),
    ASTERISK("*"),
    DASTERISK("**"),
    TILDE("~"),
    DOUBLE_QUOTE("\""),
    SINGLE_QUOTE("'"),
    SLASH("/"),
    BACKSLASH("\\"),
    QUESTION_MARK("?"),
    COLON(":"),
    SEMICOLON(";"),
    COMMA(","),
    L_PAREN("("),
    R_PAREN(")"),
    L_BRACE("{"),
    R_BRACE("}"),
    L_SQUARE("["),
    R_SQUARE("]");

    private final String[] symbols;

    PrimitiveOperator(String symbol) {
        if (symbol.isBlank()) throw new IllegalArgumentException("Cannot create blank primitive operator '" + symbol + "'");
        this.symbols = symbol.split("(?!^)");
    }

    @Override
    public String[] symbolSeq() {
        return symbols;
    }
}
