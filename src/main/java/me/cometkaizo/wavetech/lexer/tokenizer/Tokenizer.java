package me.cometkaizo.wavetech.lexer.tokenizer;

import me.cometkaizo.wavetech.lexer.CharReader;
import me.cometkaizo.wavetech.lexer.tokens.Token;
import me.cometkaizo.wavetech.lexer.tokens.TokenType;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class Tokenizer {

    public abstract Token tryTokenize(CharReader reader);

    @Nullable
    protected static <T extends TokenType> T getValidTokenType(CharReader reader, T[] tokenTypes) {
        List<T> sortedTokenTypes = new ArrayList<>(Arrays.asList(tokenTypes));
        sortBySpecificity(sortedTokenTypes);

        for (var tokenType : sortedTokenTypes) {
            var symbol = tokenType.symbol();
            if (symbol == null) continue;

            if (reader.nextSequenceEquals(symbol)) return tokenType;
        }
        return null;
    }

    private static void sortBySpecificity(List<? extends TokenType> sortedTokenTypes) {
        sortedTokenTypes.sort(((tokenType1, tokenType2) -> {
            String symbol1 = tokenType1.symbol();
            String symbol2 = tokenType2.symbol();
            if (symbol1 == null || symbol2 == null)
                return 0;
            return Double.compare(symbol2.length(), symbol1.length());
        }));
    }

}
