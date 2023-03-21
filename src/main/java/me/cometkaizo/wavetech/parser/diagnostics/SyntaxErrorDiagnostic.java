package me.cometkaizo.wavetech.parser.diagnostics;

import me.cometkaizo.wavetech.lexer.tokens.Position;

public class SyntaxErrorDiagnostic implements ParseDiagnostic {
    private final Position start;
    private final Position end;
    private final String message;

    public SyntaxErrorDiagnostic(String message, Position start, Position end) {
        this.start = start;
        this.end = end;
        this.message = message;
    }

    @Override
    public String getDiagnostic() {
        String position = start != null ?
                " at " + start.line() + ", " + start.column() +
                        (end != null && end.index() != start.index() + 1 ? " - " + end.line() + ", " + end.column() : "")
                : "";
        return "Syntax error" + position + ": \n" + message.indent(2);
    }
}
