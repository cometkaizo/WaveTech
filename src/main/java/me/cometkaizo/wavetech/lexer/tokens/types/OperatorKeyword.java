package me.cometkaizo.wavetech.lexer.tokens.types;

import me.cometkaizo.wavetech.lexer.tokens.TokenType;

public enum OperatorKeyword implements TokenType {

    AMPERSAND("&"),
    AMPERSAND_ASSIGN("&="),
    ARROW("->"),
    ASPERAND("@"),
    ASTERISK("*"),
    ASTERISK_ASSIGN("*="),
    BACKSLASH("\\"),
    CARET("^"),
    CARET_ASSIGN("^="),
    COLON(":"),
    COMMA(","),
    DECREMENT("--"),
    DOT("."),
    DOUBLE_AMPERSAND("&&"),
    DOUBLE_ASTERISK("**"),
    DOUBLE_ASTERISK_ASSIGN("**="),
    DOUBLE_COLON("::"),
    DOUBLE_EQUALS("=="),
    DOUBLE_PIPE("||"),
    DOUBLE_QUOTE("\""),
    DOUBLE_SLASH("//"),
    ELLIPSIS("..."),
    EQUALS("="),
    EXCLAMATION_MARK("!"),
    GREATER_THAN(">"),
    GREATER_THAN_OR_EQUAL(">="),
    INCREMENT("++"),
    LESS_THAN("<"),
    LESS_THAN_OR_EQUAL("<="),
    L_BRACE("{"),
    L_PAREN("("),
    L_SQUARE("["),
    MINUS("-"),
    MINUS_ASSIGN("-="),
    PERCENT("%"),
    PERCENT_ASSIGN("%="),
    PIPE("|"),
    PIPE_ASSIGN("|="),
    PLUS("+"),
    PLUS_ASSIGN("+="),
    QUESTION_MARK("?"),
    R_BRACE("}"),
    R_PAREN(")"),
    R_SQUARE("]"),
    SEMICOLON(";"),
    SINGLE_QUOTE("'"),
    SLASH("/"),
    SLASH_ASSIGN("/="),
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
