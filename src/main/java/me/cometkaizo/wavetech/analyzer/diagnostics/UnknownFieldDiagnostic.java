package me.cometkaizo.wavetech.analyzer.diagnostics;

import me.cometkaizo.wavetech.parser.structures.FieldAccessor;
import me.cometkaizo.wavetech.parser.structures.Identifier;

public class UnknownFieldDiagnostic extends ErrorDiagnostic {
    public UnknownFieldDiagnostic(FieldAccessor accessor) {
        super(createMessage(accessor.variableName), accessor.startPosition, accessor.endPosition);
    }

    private static String createMessage(Identifier variableName) {
        return "Cannot find field '" + variableName.name + "'";
    }
}
