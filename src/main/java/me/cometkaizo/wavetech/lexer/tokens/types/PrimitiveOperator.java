package me.cometkaizo.wavetech.lexer.tokens.types;

import me.cometkaizo.wavetech.lexer.tokens.TokenType;

public enum PrimitiveOperator implements TokenType {

    DOT('.'),
    EQUALS('='),
    PLUS('+'),
    MINUS('-'),
    LESS_THAN('<'),
    GREATER_THAN('>'),
    EXCLAMATION_MARK('!'),
    ASPERAND('@'),
    CARET('^'),
    AMPERSAND('&'),
    ASTERISK('*'),
    DOUBLE_QUOTE('"'),
    SINGLE_QUOTE('\''),
    SLASH('/'),
    BACKSLASH('\\'),
    QUESTION_MARK('?'),
    COLON(':'),
    SEMICOLON(';'),
    COMMA(','),
    L_PAREN('('),
    R_PAREN(')'),
    L_BRACE('{'),
    R_BRACE('}'),
    L_SQUARE('['),
    R_SQUARE(']');

    private final char character;

    PrimitiveOperator(char character) {
        this.character = character;
    }

    public char getCharacter() {
        return character;
    }

    @Override
    public String getSymbol() {
        return String.valueOf(getCharacter());
    }
}
