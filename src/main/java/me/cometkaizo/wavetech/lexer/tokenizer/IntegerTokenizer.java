package me.cometkaizo.wavetech.lexer.tokenizer;

import me.cometkaizo.wavetech.lexer.LineReader;
import me.cometkaizo.wavetech.lexer.tokens.Token;
import me.cometkaizo.wavetech.lexer.tokens.types.PrimitiveValue;

import java.util.regex.Pattern;

public class IntegerTokenizer extends StringTokenizer {

    private static final Pattern regex = Pattern.compile("[-+]?\\d+");

    @Override
    public boolean accepts(LineReader reader) {
        return regex.matcher(reader.currentWord()).matches();
    }

    @Override
    public Token tokenize(LineReader reader) {
        if (!accepts(reader)) throw new IllegalArgumentException();
        return new Token(PrimitiveValue.INT, Integer.parseInt(reader.currentWord()));
    }

}
