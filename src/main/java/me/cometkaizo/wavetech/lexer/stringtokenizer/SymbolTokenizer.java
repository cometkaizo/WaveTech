package me.cometkaizo.wavetech.lexer.stringtokenizer;

import me.cometkaizo.wavetech.lexer.LineReader;
import me.cometkaizo.wavetech.lexer.tokens.Token;
import me.cometkaizo.wavetech.lexer.tokens.types.Keywords;
import me.cometkaizo.wavetech.lexer.tokens.types.ObjectType;

public class SymbolTokenizer extends StringTokenizer {

    @Override
    public boolean accepts(LineReader reader) {
        return Keywords.isValidSymbolName(reader.currentWord());
    }

    @Override
    public Token tokenize(LineReader reader) {
        if (!accepts(reader)) throw new IllegalArgumentException();
        return new Token(ObjectType.SYMBOL_OR_REFERENCE, reader.currentWord());
    }
}
