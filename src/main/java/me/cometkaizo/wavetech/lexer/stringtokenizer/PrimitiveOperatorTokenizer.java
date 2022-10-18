package me.cometkaizo.wavetech.lexer.stringtokenizer;

import me.cometkaizo.wavetech.lexer.LineReader;
import me.cometkaizo.wavetech.lexer.tokens.Token;
import me.cometkaizo.wavetech.lexer.tokens.types.Keywords;

public class PrimitiveOperatorTokenizer extends StringTokenizer {

    @Override
    public boolean accepts(LineReader reader) {
        if (reader.currentWord().length() != 1) return false;
        return Keywords.isPrimitiveOperator(reader.currentChar());
    }

    @Override
    public Token tokenize(LineReader reader) {
        if (!accepts(reader)) throw new IllegalArgumentException();
        return new Token(Keywords.parsePrimitiveOperator(reader.currentChar()));
    }

}
