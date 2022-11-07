package me.cometkaizo.wavetech.lexer.tokenizer;

import me.cometkaizo.wavetech.lexer.LineReader;
import me.cometkaizo.wavetech.lexer.tokens.Token;
import me.cometkaizo.wavetech.lexer.tokens.types.Keywords;

public class PropertyModifierTokenizer extends StringTokenizer {

    @Override
    public boolean accepts(LineReader reader) {
        return Keywords.isAtModifierKeyword(reader);
    }

    @Override
    public Token tokenize(LineReader reader) {
        if (!accepts(reader)) throw new IllegalArgumentException();
        return new Token(Keywords.parseModifierKeyword(reader));
    }
}
