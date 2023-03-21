package me.cometkaizo.wavetech.lexer;

import me.cometkaizo.util.StringUtils;
import me.cometkaizo.wavetech.lexer.tokenizer.Tokenizer;
import me.cometkaizo.wavetech.lexer.tokenizer.Tokenizers;
import me.cometkaizo.wavetech.lexer.tokens.Token;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Lexer {

    private CharReader charReader;
    private final List<Token> tokens = new ArrayList<>();


    public List<Token> tokenize(File file) {
        throwIfInvalidFile(file);

        charReader = newCharReader(file);

        tokenizeLines();
        return tokens;
    }

    @NotNull
    private static CharReader newCharReader(File file) {
        try {
            return new CharReader(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private void tokenizeLines() {

        while (charReader.hasNext()) {
            charReader.skipWhitespace();
            readNextChar();
        }

    }

    private void readNextChar() {
        int startCursor = charReader.cursor();

        List<Tokenizer> tokenizers = Tokenizers.tokenizers;
        for (Tokenizer tokenizer : tokenizers) {
            var token = tokenizer.tryTokenize(charReader);

            if (token != null) {
                addToken(token);
                return;
            }

            charReader.jumpTo(startCursor);
        }

        throw newIllegalSymbolException(charReader.current());
    }

    @NotNull
    private CompilationException newIllegalSymbolException(char character) {

        String message = "Could not lex character '" + character + "' with unicode: \\u" + StringUtils.unicodeOf(character);

        return new CompilationException(
                message,
                charReader.getLine(), charReader.getCol());
    }

    private void addToken(Token token) {
        tokens.add(token);
    }

    private static void throwIfInvalidFile(File file) {
        if (!file.exists() || file.isDirectory()) {
            throw new IllegalArgumentException("'" + file.getPath() + "' does not exist or is a directory");
        }
        if (file.getName().isBlank())
            throw new IllegalArgumentException("Illegal name '" + file.getName() + "' for file");
    }

}
