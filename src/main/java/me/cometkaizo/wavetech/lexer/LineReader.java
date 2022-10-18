package me.cometkaizo.wavetech.lexer;

import me.cometkaizo.wavetech.lexer.tokens.types.Keywords;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class LineReader {

    private final List<String> words;
    private final List<Integer> wordLines;
    private final List<Integer> wordCols;
    private int currentCharIndex = 0;
    private int currentWordIndex = -1;
    private char currentChar;
    private String currentWord;

    public LineReader(List<String> lines) {
        lines.removeIf(String::isBlank);

        words = new ArrayList<>(lines.size());
        wordLines = new ArrayList<>(lines.size());
        wordCols = new ArrayList<>(lines.size());
        makeWords(lines);
    }

    private void makeWords(List<String> lines) {
        for (int lineIndex = 0; lineIndex < lines.size(); lineIndex++) {

            String line = lines.get(lineIndex).trim().replaceAll("\\s{2,}", " ");

            for (int charIndex = 0; charIndex < line.length(); charIndex ++) {

                char currentChar = line.charAt(charIndex);
                if (Character.isWhitespace(currentChar)) continue;

                StringBuilder wordBuilder = new StringBuilder(String.valueOf(currentChar));

                wordLines.add(lineIndex + 1);
                wordCols.add(charIndex + 1);

                if (!Keywords.isPrimitiveOperator(currentChar)) {

                    boolean hasNextChar = charIndex < line.length() - 1;
                    char nextChar = hasNextChar ? line.charAt(charIndex + 1) : ' ';

                    while (hasNextChar && !Character.isWhitespace(nextChar) && !Keywords.isPrimitiveOperator(nextChar)) {
                        wordBuilder.append(nextChar);

                        charIndex++;
                        hasNextChar = charIndex < line.length() - 1;
                        nextChar = hasNextChar ? line.charAt(charIndex + 1) : ' ';
                    }
                }

                words.add(wordBuilder.toString());
            }
        }
    }

    public LineReader(File file) {
        this(readLines(file));
    }

    @NotNull
    private static List<String> readLines(File file) {
        try {
            return Files.readAllLines(file.toPath());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public boolean hasNextWord() {
        return currentWordIndex < words.size() - 1;
    }
    public boolean hasNextChar() {
        return currentCharIndex < currentWord.length() - 1 || hasNextWord();
    }

    public char nextChar() {
        advanceChar();
        return currentChar;
    }

    public String nextWord() {

        if (!hasNextWord()) throw new IllegalStateException("No next word to get");

        return words.get(++currentWordIndex);
    }

    public String peekWord() {
        return peekWord(1);
    }
    public String peekWord(int amount) {
        if (!hasNextWord()) throw new IllegalStateException("No word to peek");
        return words.get(currentWordIndex + amount);

    }
    public char peek() {
        return peek(1);
    }
    public char peek(int amount) {
        if (!hasNextChar()) throw new IllegalStateException("No char to peek");

        int amountLeft = amount;
        int _charIndex = currentCharIndex;
        String _currentWord = currentWord;

        while (_charIndex + amountLeft >= _currentWord.length()) {

            amountLeft -= _currentWord.length() - _charIndex;

            if (!hasNextWord()) {
                throw new IllegalStateException("No char to peek at amount " + amount);
            }
            _currentWord = peekWord();
            _charIndex = 0;
        }

        return _currentWord.charAt(_charIndex + amountLeft);
    }

    public void advanceChar() {
        if (!hasNextChar()) throw new IllegalStateException("No char to advance at end of file");
        currentCharIndex ++;

        if (currentCharIndex == currentWord.length())
            advanceWord();

        currentChar = currentWord.charAt(currentCharIndex);
    }
    public void advanceWord() {
        if (!hasNextWord()) throw new IllegalStateException("No word to advance at end of file");
        currentWordIndex ++;
        currentCharIndex = 0;

        currentWord = words.get(currentWordIndex);
    }

    public char currentChar() {
        return words.get(currentWordIndex).charAt(currentCharIndex);
    }

    public int getLine() {
        return wordLines.get(currentWordIndex);
    }

    public int getCol() {
        return wordCols.get(currentWordIndex);
    }

    public String currentWord() {
        return words.get(currentWordIndex);
    }
}
