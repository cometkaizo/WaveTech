package me.cometkaizo.wavetech.lexer.tokenizer;

import me.cometkaizo.wavetech.lexer.LineReader;
import me.cometkaizo.wavetech.lexer.tokens.Token;
import me.cometkaizo.wavetech.lexer.tokens.types.PrimitiveValue;

public class BooleanTokenizer extends StringTokenizer {

    @Override
    public boolean accepts(LineReader reader) {
        String string = reader.currentWord();
        return string.equals("true") || string.equals("false");
    }

    @Override
    public Token tokenize(LineReader reader) {
        if (!accepts(reader)) throw new IllegalArgumentException();
        return new Token(PrimitiveValue.BOOLEAN, reader.currentWord().equals("true"));
    }
}
