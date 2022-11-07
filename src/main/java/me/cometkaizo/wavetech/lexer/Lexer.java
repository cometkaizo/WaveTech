package me.cometkaizo.wavetech.lexer;

import me.cometkaizo.util.LogUtils;
import me.cometkaizo.wavetech.lexer.tokenizer.StringTokenizers;
import me.cometkaizo.wavetech.lexer.tokenizer.StringTokenizer;
import me.cometkaizo.wavetech.lexer.tokens.Token;
import me.cometkaizo.wavetech.lexer.tokens.types.Keywords;
import me.cometkaizo.wavetech.lexer.tokens.types.ObjectType;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Lexer {

    private LineReader lineReader;
    private final List<Token> tokens = new ArrayList<>();


    public List<Token> tokenize(File file) {
        throwIfInvalidFile(file);
        tokens.add(new Token(ObjectType.SYMBOL, file.getName()));

        lineReader = new LineReader(file);

        tokenizeLines();
        return tokens;
    }



    private void tokenizeLines() {

        while (lineReader.hasNext()) {
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
        LogUtils.debug("symbol {}", symbol);
        for (int peekAmt = 0, len = symbol.length(); peekAmt < len; peekAmt++) {
            throwIfInvalidChar(peekAmt);
        }
    }

    private void throwIfInvalidChar(int peekAmt) {
        char peekedChar = lineReader.peekChar(peekAmt);

        if (!Keywords.isValidSymbolName(String.valueOf(peekedChar))
                && !Keywords.isAtPrimitiveOperator(lineReader)
                && !Keywords.isNumber(peekedChar)) {
            throw new CompilationException("Illegal character '" + peekedChar + "'", lineReader.getLine(), lineReader.getCol());
        }
    }

    private static void throwIfInvalidFile(File file) {
        if (!file.exists() || file.isDirectory()) {
            throw new IllegalArgumentException("File does not exist or is directory");
        }
        if (file.getName().isBlank())
            throw new IllegalArgumentException("Illegal name '" + file.getName() + "' for file");
    }

}
