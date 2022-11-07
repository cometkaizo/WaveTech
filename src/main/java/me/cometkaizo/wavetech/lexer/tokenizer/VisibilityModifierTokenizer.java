package me.cometkaizo.wavetech.lexer.tokenizer;

import me.cometkaizo.wavetech.lexer.LineReader;
import me.cometkaizo.wavetech.lexer.tokens.Token;
import me.cometkaizo.wavetech.lexer.tokens.types.Keywords;

public class VisibilityModifierTokenizer extends StringTokenizer {

    @Override
    public boolean accepts(LineReader reader) {
        return Keywords.isAtVisibilityKeyword(reader);
    }

    @Override
    public Token tokenize(LineReader reader) {
        if (!accepts(reader)) throw new IllegalArgumentException();
        return new Token(Keywords.parseVisibilityKeyword(reader));
    }
}
