package me.cometkaizo.wavetech.lexer.stringtokenizer;

import me.cometkaizo.wavetech.lexer.LineReader;
import me.cometkaizo.wavetech.lexer.tokens.Token;
import me.cometkaizo.wavetech.lexer.tokens.types.PrimitiveType;

public class BooleanTokenizer extends StringTokenizer {

    @Override
    public boolean accepts(LineReader reader) {
        String string = reader.currentWord();
        return string.equals("true") || string.equals("false");
    }

    @Override
    public Token tokenize(LineReader reader) {
        if (!accepts(reader)) throw new IllegalArgumentException();
        return new Token(PrimitiveType.BOOLEAN, reader.currentWord().equals("true"));
    }
}
