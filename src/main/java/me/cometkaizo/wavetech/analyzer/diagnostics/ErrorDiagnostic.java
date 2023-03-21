package me.cometkaizo.wavetech.analyzer.diagnostics;

import me.cometkaizo.wavetech.lexer.tokens.Position;

public class ErrorDiagnostic implements Diagnostic {

    private final Position start;
    private final Position end;
    private final String message;

    public ErrorDiagnostic(String message, Position start, Position end) {
        this.message = message;
        this.start = start;
        this.end = end;
    }

    @Override
    public String getDiagnostic() {
        String position = start != null ?
                " at " + start.line() + ", " + start.column() +
                        (end != null && end.index() != start.index() + 1 ? " - " + end.line() + ", " + end.column() : "")
                : "";
        return "Error" + position + ": \n" + message.indent(2);
    }

}
