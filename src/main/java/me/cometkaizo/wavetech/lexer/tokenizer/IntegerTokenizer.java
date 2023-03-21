package me.cometkaizo.wavetech.lexer.tokenizer;

import me.cometkaizo.util.PeekingIterator;
import me.cometkaizo.wavetech.lexer.CharReader;
import me.cometkaizo.wavetech.lexer.tokens.Token;
import me.cometkaizo.wavetech.lexer.tokens.types.Literal;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class IntegerTokenizer extends Tokenizer {

    private static final Map<Character, Integer> CHAR_TO_INT = Map.of(
            '0', 0,
            '1', 1,
            '2', 2,
            '3', 3,
            '4', 4,
            '5', 5,
            '6', 6,
            '7', 7,
            '8', 8,
            '9', 9
    );

    @Override
    public Token tryTokenize(CharReader reader) {
        var start = reader.getPosition();

        Integer value = readInteger(reader);

        if (value == null) return null;
        return new Token(Literal.INT, value, start, reader.getPosition());
    }

    @Nullable
    public static Integer readInteger(PeekingIterator<Character> characters) {
        if (!characters.hasNext()) return null;
        if (!isDigit(characters.current())) return null;

        long number = 0;
        while (true) {
            if (characters.hasNext() && isDigit(characters.current())) {

                number *= 10;
                number += getDigit(characters.next());
                if (number > Integer.MAX_VALUE) return null;

            } else return (int) number;
        }
    }

    private static boolean isDigit(char character) {
        return CHAR_TO_INT.containsKey(character);
    }

    private static int getDigit(char character) {
        return CHAR_TO_INT.get(character);
    }

}
