package me.cometkaizo.wavetech.lexer.tokenizer;

import me.cometkaizo.wavetech.lexer.CharReader;
import me.cometkaizo.wavetech.lexer.tokens.Token;
import me.cometkaizo.wavetech.lexer.tokens.types.TypeKeyword;

import java.util.regex.Pattern;

public class SymbolTokenizer extends Tokenizer {


    private static final Pattern firstLetterRegex = Pattern.compile("[a-zA-Z$_]");
    private static final Pattern letterRegex = Pattern.compile("[a-zA-Z0-9$_]");

    @Override
    public Token tryTokenize(CharReader reader) {
        if (!reader.hasNext()) return null;
        var start = reader.getPosition();
        StringBuilder builder = new StringBuilder();

        String firstLetter = String.valueOf(reader.next());
        if (!firstLetterRegex.matcher(firstLetter).matches()) return null;

        builder.append(firstLetter);
        while (true) {
            String letter = String.valueOf(reader.current());
            if (!letterRegex.matcher(letter).matches())
                return new Token(TypeKeyword.SYMBOL, builder.toString(), start, reader.getPosition());

            builder.append(letter);
            reader.advance();
        }
    }
}
