package me.cometkaizo.wavetech.parser;

import me.cometkaizo.wavetech.lexer.CompilationException;

public class IllegalTokenException extends CompilationException {

    public IllegalTokenException() {
    }

    public IllegalTokenException(String message) {
        super(message);
    }

    public IllegalTokenException(String message, Throwable cause) {
        super(message, cause);
    }

    public IllegalTokenException(Throwable cause) {
        super(cause);
    }
}
