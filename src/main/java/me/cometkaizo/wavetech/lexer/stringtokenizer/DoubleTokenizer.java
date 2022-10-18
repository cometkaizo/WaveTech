package me.cometkaizo.wavetech.lexer.stringtokenizer;

import me.cometkaizo.wavetech.lexer.LineReader;
import me.cometkaizo.wavetech.lexer.tokens.Token;
import me.cometkaizo.wavetech.lexer.tokens.types.PrimitiveValue;

import java.util.regex.Pattern;

public class DoubleTokenizer extends StringTokenizer {

    private static final Pattern integer = Pattern.compile("[-+]?\\d+");
    private static final Pattern explicitDouble = Pattern.compile("[-+]?\\d+[Dd]");
    private static final Pattern decimal = Pattern.compile("\\d+");


    @Override
    public boolean accepts(LineReader reader) {
        if (explicitDouble.matcher(reader.currentWord()).matches()) return true;
        if (!integer.matcher(reader.currentWord()).matches()) return false;
        if (!".".equals(reader.peekWord())) return false;
        return decimal.matcher(reader.peekWord(2)).matches();
    }

    @Override
    public Token tokenize(LineReader reader) {
        if (!accepts(reader)) throw new IllegalArgumentException();

        boolean isExplicitDouble = explicitDouble.matcher(reader.currentWord()).matches();
        if (isExplicitDouble)
            return new Token(PrimitiveValue.DOUBLE, Double.parseDouble(reader.currentWord()));

        return new Token(PrimitiveValue.DOUBLE,
                Double.parseDouble(reader.currentWord() + reader.nextWord() + reader.nextWord())
        );
    }
}
