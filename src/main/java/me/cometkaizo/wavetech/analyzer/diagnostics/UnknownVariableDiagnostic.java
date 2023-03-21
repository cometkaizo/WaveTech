package me.cometkaizo.wavetech.analyzer.diagnostics;

import me.cometkaizo.wavetech.parser.structures.Identifier;
import me.cometkaizo.wavetech.parser.structures.VariableAccessor;

public class UnknownVariableDiagnostic extends ErrorDiagnostic {
    public UnknownVariableDiagnostic(VariableAccessor accessor) {
        super(createMessage(accessor.variableName), accessor.startPosition, accessor.endPosition);
    }

    private static String createMessage(Identifier variableName) {
        return "Cannot find variable '" + variableName.name + "'";
    }
}
