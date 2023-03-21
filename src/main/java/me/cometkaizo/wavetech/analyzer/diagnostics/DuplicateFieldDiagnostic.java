package me.cometkaizo.wavetech.analyzer.diagnostics;

import me.cometkaizo.wavetech.lexer.tokens.Position;
import me.cometkaizo.wavetech.parser.structures.ClassStructure;
import me.cometkaizo.wavetech.parser.structures.FieldDeclaration;

public class DuplicateFieldDiagnostic extends ErrorDiagnostic {
    public DuplicateFieldDiagnostic(FieldDeclaration offendingField, FieldDeclaration existingField, ClassStructure clazz) {
        super(createMessage(offendingField, existingField, clazz), offendingField.startPosition, offendingField.endPosition);
    }

    private static String createMessage(FieldDeclaration offendingField, FieldDeclaration existingField, ClassStructure clazz) {
        String name = offendingField.name.name;
        String className = clazz.name.name;
        Position existingFieldPos = existingField.startPosition;
        return "Field '" + name + "' is already declared in '" + className + "' at " + existingFieldPos.line() + ", " + existingFieldPos.column();
    }
}
