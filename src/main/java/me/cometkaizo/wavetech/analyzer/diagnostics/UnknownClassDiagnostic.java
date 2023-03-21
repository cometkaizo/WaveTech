package me.cometkaizo.wavetech.analyzer.diagnostics;

import me.cometkaizo.wavetech.parser.structures.ClassAccessor;
import me.cometkaizo.wavetech.parser.structures.Identifier;

public class UnknownClassDiagnostic extends ErrorDiagnostic {
    public UnknownClassDiagnostic(ClassAccessor accessor) {
        super(createMessage(accessor.className), accessor.startPosition, accessor.endPosition);
    }

    private static String createMessage(Identifier variableName) {
        return "Cannot find class '" + variableName.name + "'";
    }
}
