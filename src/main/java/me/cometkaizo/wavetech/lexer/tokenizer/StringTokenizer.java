package me.cometkaizo.wavetech.lexer.tokenizer;

import me.cometkaizo.wavetech.lexer.LineReader;
import me.cometkaizo.wavetech.lexer.tokens.Token;

public abstract class StringTokenizer {

    public abstract boolean accepts(LineReader reader);
    public abstract Token tokenize(LineReader reader);

}
