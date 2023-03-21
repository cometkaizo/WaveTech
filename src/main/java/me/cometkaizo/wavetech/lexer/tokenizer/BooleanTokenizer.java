package me.cometkaizo.wavetech.lexer.tokenizer;

import me.cometkaizo.wavetech.lexer.CharReader;
import me.cometkaizo.wavetech.lexer.tokens.Token;
import me.cometkaizo.wavetech.lexer.tokens.types.Literal;

public class BooleanTokenizer extends Tokenizer {

    private static final String FALSE = "false";
    private static final String TRUE = "true";


    @Override
    public Token tryTokenize(CharReader reader) {
        var start = reader.getPosition();

        if (reader.nextSequenceEquals(FALSE)) return new Token(Literal.BOOLEAN, false, start, reader.getPosition());
        if (reader.nextSequenceEquals(TRUE)) return new Token(Literal.BOOLEAN, true, start, reader.getPosition());
        return null;
    }

}
