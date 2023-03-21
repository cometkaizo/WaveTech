package me.cometkaizo.wavetech.lexer;

import me.cometkaizo.logging.LogUtils;
import me.cometkaizo.util.CollectionUtils;
import me.cometkaizo.util.PeekingIterator;
import me.cometkaizo.util.StringUtils;
import me.cometkaizo.wavetech.lexer.tokens.Position;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class CharReader extends PeekingIterator<Character> {

    private int line = 1;
    private int col = 1;

    public CharReader(char[] chars) {
        this(CollectionUtils.box(chars));
    }
    public CharReader(@NotNull Character[] chars) {
        super(chars);
        LogUtils.debug("chars: \n{}", Arrays.stream(chars).map(Objects::toString).collect(Collectors.joining("")));
    }
    public CharReader(@NotNull List<Character> chars) {
        this(chars.toArray(Character[]::new));
    }
    public CharReader(@NotNull File file) throws IOException {
        this(readChars(file));
    }
    private static char[] readChars(@NotNull File file) throws IOException {
        List<String> lines = Files.readAllLines(file.toPath());
        String content = StringUtils.join(lines, "\n");
        return content.toCharArray();
    }


    @Override
    public void advance(int amt) {
        throwIfIllegalAdvance(amt);

        int amtSign = (int) Math.signum(amt); // -1, 0, 1
        while (amt != 0) {

            if (index != -1) {
                updateLineAndColumn(amtSign);
            }

            index += amtSign;
            amt -= amtSign;
        }
    }

    private void updateLineAndColumn(int amtSign) {
        if (amtSign > 0 && current() == '\n') {
            line ++;
            col = 1;
        } else if (amtSign < 0 && peekPreviousSequenceEquals("\n")) {
            line --;
            col = getCharsSinceLastNewline();
        } else if (index + amtSign != -1) { // index -1 does not count as a column
            col += amtSign;
        }
    }

    private int getCharsSinceLastNewline() {
        int charsSinceLastNewline = 1;
        while (index - charsSinceLastNewline - 1 >= 0 && peek(-charsSinceLastNewline - 1) != '\n') {
            charsSinceLastNewline ++;
        }
        return charsSinceLastNewline;
    }

    @Override
    public void jumpTo(int index) {
        throwIfIllegalIndex(index);

        advance(index - this.index);
    }

    @Override
    public void reset() {
        super.reset();
        line = 1;
        col = 1;
    }

    public boolean nextSequenceEquals(CharSequence sequence) {
        if (!hasNext(sequence.length())) return false; // if too little characters left then break

        int startCursor = cursor();
        for (int charIndex = 0; charIndex < sequence.length(); charIndex ++) {

            // get required & actual
            char required = sequence.charAt(charIndex);
            char actual = next();

            // compare
            if (required != actual) {
                jumpTo(startCursor);
                return false;
            }
        }
        return true;
    }
    public boolean peekNextSequenceEquals(CharSequence sequence) {
        if (!hasNext(sequence.length())) return false; // if too little characters left then break

        int startCursor = cursor();
        for (int charIndex = 0; charIndex < sequence.length(); charIndex ++) {

            // get required & actual
            char required = sequence.charAt(charIndex);
            char actual = peek(charIndex);

            // compare
            if (required != actual) {
                jumpTo(startCursor);
                return false;
            }
        }
        return true;
    }

    public boolean previousSequenceEquals(CharSequence sequence) {
        if (peekPreviousSequenceEquals(sequence)) {
            jumpTo(index - sequence.length());
            return true;
        }
        return false;
    }
    public boolean peekPreviousSequenceEquals(CharSequence sequence) {
        if (!hasPrevious(sequence.length())) return false; // if too little characters left then break

        for (int charIndex = 0; charIndex < sequence.length(); charIndex ++) {

            // get required & actual
            char required = sequence.charAt(charIndex);
            char actual = peek(- (sequence.length() - charIndex));

            // compare
            if (required != actual) {
                return false;
            }
        }
        return true;
    }

    /**
     * Advances past whitespace such that {@link CharReader#next()} will return non-whitespace, and {@link CharReader#current()} may return whitespace.
     */
    public void skipWhitespace() {
        while (Character.isWhitespace(current())) {
            advance();
        }
    }


    public int getLine() {
        return line;
    }

    public int getCol() {
        return col;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        CharReader that = (CharReader) o;
        return line == that.line && col == that.col;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), line, col);
    }

    public Position getPosition() {
        return new Position(index, line, col);
    }
}
