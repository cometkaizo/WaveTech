package me.cometkaizo.wavetech.analyzer.diagnostics;

import me.cometkaizo.wavetech.parser.structures.Identifier;

public class IllegalSelfReferenceDiagnostic extends ErrorDiagnostic {
    public IllegalSelfReferenceDiagnostic(Identifier variableName) {
        super(createMessage(variableName), variableName.startPosition, variableName.endPosition);
    }

    private static String createMessage(Identifier variableName) {
        return "Illegal self reference: " + variableName.name + "";
    }
}
