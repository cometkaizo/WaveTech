package me.cometkaizo.wavetech.analyzer.diagnostics;

import me.cometkaizo.wavetech.parser.structures.ClassStructure;
import me.cometkaizo.wavetech.syntaxes.SyntaxStructure;

public class IncompatibleTypesDiagnostic extends ErrorDiagnostic {
    public IncompatibleTypesDiagnostic(ClassStructure actualType, ClassStructure expectedType, SyntaxStructure offendingStructure) {
        super(createMessage(actualType, expectedType), offendingStructure.getStartPosition(), offendingStructure.getEndPosition());
    }

    private static String createMessage(ClassStructure actualType, ClassStructure expectedType) {
        String actualTypeName = actualType != null ? actualType.name.name : "[Unknown]";
        String expectedTypeName = expectedType != null ? expectedType.name.name : "[Unknown]";

        return "Type '" + actualTypeName + "' cannot be cast to required type '" + expectedTypeName + '\'';
    }
}
