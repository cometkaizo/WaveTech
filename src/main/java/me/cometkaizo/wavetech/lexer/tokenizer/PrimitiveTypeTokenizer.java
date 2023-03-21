package me.cometkaizo.wavetech.lexer.tokenizer;

import me.cometkaizo.wavetech.lexer.CharReader;
import me.cometkaizo.wavetech.lexer.tokens.Token;
import me.cometkaizo.wavetech.lexer.tokens.types.PrimitiveType;

public class PrimitiveTypeTokenizer extends Tokenizer {

    @Override
    public Token tryTokenize(CharReader reader) {
        var start = reader.getPosition();

        var result = getValidTokenType(reader, PrimitiveType.values());
        if (result == null) return null;

        return new Token(result, start, reader.getPosition());
    }
}
