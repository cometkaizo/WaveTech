package me.cometkaizo.wavetech.lexer.tokenizer;

import me.cometkaizo.wavetech.lexer.LineReader;
import me.cometkaizo.wavetech.lexer.tokens.Token;
import me.cometkaizo.wavetech.lexer.tokens.types.PrimitiveValue;

public class NullTokenizer extends StringTokenizer {

    @Override
    public boolean accepts(LineReader reader) {
        return "null".equals(reader.currentWord());
    }

    @Override
    public Token tokenize(LineReader reader) {
        if (!accepts(reader)) throw new IllegalArgumentException();
        return new Token(PrimitiveValue.NULL);
    }
}
