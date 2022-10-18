package me.cometkaizo.wavetech.lexer;

public class CompilationException extends RuntimeException {

    private final Integer line;
    private final Integer col;

    public CompilationException() {
        line = null;
        col = null;
    }

    public CompilationException(String message) {
        super(message);
        line = null;
        col = null;
    }

    public CompilationException(String message, Throwable cause) {
        super(message, cause);
        line = null;
        col = null;
    }

    public CompilationException(Throwable cause) {
        super(cause);
        line = null;
        col = null;
    }

    public CompilationException(int line, int col) {
        this.line = line;
        this.col = col;
    }

    public CompilationException(String message, int line, int col) {
        super(message);
        this.line = line;
        this.col = col;
    }

    public CompilationException(String message, int line, int col, Throwable cause) {
        super(message, cause);
        this.line = line;
        this.col = col;
    }

    public CompilationException(int line, int col, Throwable cause) {
        super(cause);
        this.line = line;
        this.col = col;
    }

    @Override
    public String getMessage() {
        return super.getMessage() + "\nCaused at line " +
                (line == null ? "UNKNOWN" : line + 1) +
                ", column " + (col == null ? "UNKNOWN" : "~" + (col + 1));
    }
}
