package me.cometkaizo.wavetech.parser;

import me.cometkaizo.wavetech.lexer.CompilationException;

public class UnexpectedTokenException extends CompilationException {

    public UnexpectedTokenException() {
    }

    public UnexpectedTokenException(String message) {
        super(message);
    }

    public UnexpectedTokenException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnexpectedTokenException(Throwable cause) {
        super(cause);
    }


}
