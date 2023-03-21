package me.cometkaizo.wavetech.lexer.tokenizer;

import me.cometkaizo.util.MathUtils;
import me.cometkaizo.wavetech.lexer.CharReader;
import me.cometkaizo.wavetech.lexer.tokens.Token;
import me.cometkaizo.wavetech.lexer.tokens.types.Literal;

public class DoubleTokenizer extends Tokenizer {

    @Override
    public Token tryTokenize(CharReader reader) {
        var start = reader.getPosition();

        Integer whole = IntegerTokenizer.readInteger(reader);
        if (whole == null) return null;

        if (!reader.hasNext() || reader.next() != '.') return null;

        Integer decimal = IntegerTokenizer.readInteger(reader);
        if (decimal == null) return null;

        if (reader.hasNext() && (reader.next() == 'D' || reader.next() == 'd')) reader.advance();

        double number = whole + decimal / Math.pow(10, MathUtils.digitCount(decimal));
        return new Token(Literal.DOUBLE, number, start, reader.getPosition());
    }
}
