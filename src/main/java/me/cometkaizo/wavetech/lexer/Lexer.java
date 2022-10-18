package me.cometkaizo.wavetech.lexer;

import me.cometkaizo.util.LogUtils;
import me.cometkaizo.wavetech.lexer.stringtokenizer.StringTokenizers;
import me.cometkaizo.wavetech.lexer.stringtokenizer.StringTokenizer;
import me.cometkaizo.wavetech.lexer.tokens.Token;
import me.cometkaizo.wavetech.lexer.tokens.types.Keywords;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Lexer {

    private LineReader lineReader;
    private final List<Token> tokens = new ArrayList<>();


    public List<Token> tokenize(File file) {
        throwIfInvalidFile(file);

        lineReader = new LineReader(file);

        tokenizeLines();
        return tokens;
    }



    private void tokenizeLines() {

        while (lineReader.hasNextWord()) {
            lineReader.advanceWord();

            readSymbol();
        }

    }

    private void readSymbol() {
        String symbol = lineReader.currentWord();

        LogUtils.info("Lexing symbol '{}'", symbol);
        throwIfInvalidSymbol(symbol);

        Optional<StringTokenizer> tokenizer = StringTokenizers.tokenizers.stream().filter(parser -> parser.accepts(lineReader)).findFirst();

        if (tokenizer.isPresent()) tokens.add(tokenizer.get().tokenize(lineReader));
        else throw new CompilationException("Unknown symbol '" + symbol + "' cannot be lexed", lineReader.getLine(), lineReader.getCol());
    }

    private void throwIfInvalidSymbol(String symbol) {
        for (int i : symbol.chars().toArray()) {
            throwIfInvalidChar((char) i);
        }
    }

    private void throwIfInvalidChar(char c) {
        if (!Keywords.isValidSymbolName(String.valueOf(c))
                && !Keywords.isPrimitiveOperator(c)
                && !Keywords.isNumber(c))
            throw new CompilationException("Illegal character '" + c + "'", lineReader.getLine(), lineReader.getCol());
    }

    private static void throwIfInvalidFile(File file) {
        if (!file.exists() || file.isDirectory()) {
            LogUtils.error("File does not exist or is directory exception");
            throw new IllegalArgumentException("File does not exist or is directory");
        }
    }

}
