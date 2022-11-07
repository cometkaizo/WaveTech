package me.cometkaizo.wavetech.lexer.tokenizer;

import me.cometkaizo.util.LogUtils;
import me.cometkaizo.wavetech.lexer.LineReader;
import me.cometkaizo.wavetech.lexer.tokens.Token;
import me.cometkaizo.wavetech.lexer.tokens.types.Keywords;
import me.cometkaizo.wavetech.lexer.tokens.types.PrimitiveOperator;

public class PrimitiveOperatorTokenizer extends StringTokenizer {

    @Override
    public boolean accepts(LineReader reader) {
        if (reader.currentWord().length() != 1) return false;
        return Keywords.isAtPrimitiveOperator(reader);
    }

    @Override
    public Token tokenize(LineReader reader) {
        if (!accepts(reader)) throw new IllegalArgumentException();

        LogUtils.warn("reader's current word: {}, peeking word: {}",
                reader.currentWord(),
                (reader.hasNext()? reader.peekWord() : null));
        PrimitiveOperator result = Keywords.parsePrimitiveOperator(reader);
        LogUtils.warn("parsed primitive operator: {}", result);
        return new Token(result);
    }

}
