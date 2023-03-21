package me.cometkaizo.wavetech.analyzer.diagnostics;

import me.cometkaizo.wavetech.parser.structures.FieldDeclaration;
import me.cometkaizo.wavetech.parser.structures.Identifier;

public class IllegalForwardReferenceDiagnostic extends ErrorDiagnostic {
    public IllegalForwardReferenceDiagnostic(Identifier referencedFieldName, FieldDeclaration field) {
        super(createMessage(referencedFieldName, field), referencedFieldName.startPosition, referencedFieldName.endPosition);
    }

    private static String createMessage(Identifier referencedFieldName, FieldDeclaration field) {
        return "Illegal forward reference to '" + referencedFieldName.name + "' from '" + field.name.name + "'";
    }
}
