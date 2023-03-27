package me.cometkaizo.wavetech.lexer.tokens.types;

import me.cometkaizo.wavetech.lexer.tokens.TokenType;

public enum OperatorKeyword implements TokenType {

    AMPERSAND("&"),
    AMPERSAND_EQUALS("&="),
    ARROW("->"),
    ASPERAND("@"),
    ASTERISK("*"),
    ASTERISK_EQUALS("*="),
    BACKSLASH("\\"),
    CARET("^"),
    CARET_EQUALS("^="),
    COLON(":"),
    COMMA(","),
    DECREMENT("--"),
    DOT("."),
    DOUBLE_AMPERSAND("&&"),
    DOUBLE_ASTERISK("**"),
    DOUBLE_ASTERISK_EQUALS("**="),
    DOUBLE_COLON("::"),
    DOUBLE_EQUALS("=="),
    DOUBLE_MINUS("--"),
    DOUBLE_PIPE("||"),
    DOUBLE_PLUS("++"),
    DOUBLE_QUOTE("\""),
    DOUBLE_SLASH("//"),
    ELLIPSIS("..."),
    EQUALS("="),
    EXCLAMATION_MARK("!"),
    EXCLAMATION_MARK_EQUALS("!="),
    GREATER_THAN(">"),
    GREATER_THAN_OR_EQUAL(">="),
    INCREMENT("++"),
    LESS_THAN("<"),
    LESS_THAN_OR_EQUAL("<="),
    L_BRACE("{"),
    L_PAREN("("),
    L_SQUARE("["),
    MINUS("-"),
    MINUS_EQUALS("-="),
    PERCENT("%"),
    PERCENT_EQUALS("%="),
    L_SHIFT("<<"),
    R_SHIFT(">>"),
    TRIPLE_R_SHIFT(">>>"),
    L_SHIFT_EQUALS("<<="),
    R_SHIFT_EQUALS(">>="),
    TRIPLE_R_SHIFT_EQUALS(">>>="),
    PIPE("|"),
    PIPE_EQUALS("|="),
    PLUS("+"),
    PLUS_EQUALS("+="),
    QUESTION_MARK("?"),
    R_BRACE("}"),
    R_PAREN(")"),
    R_SQUARE("]"),
    SEMICOLON(";"),
    SINGLE_QUOTE("'"),
    SLASH("/"),
    SLASH_EQUALS("/="),
    STRONG_ARROW("=>"),
    TILDE("~"),
    TRIPLE_QUOTE("\"\"\"");

    private final String symbol;

    OperatorKeyword(String symbol) {
        if (symbol.isBlank())
            throw new IllegalArgumentException("Cannot create blank primitive operator '" + this + "' : '" + symbol + "'");
        this.symbol = symbol;
    }

    @Override
    public String symbol() {
        return symbol;
    }

}
