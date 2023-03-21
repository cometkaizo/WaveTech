package me.cometkaizo.wavetech.analyzer.diagnostics;

import me.cometkaizo.wavetech.lexer.tokens.Position;
import me.cometkaizo.wavetech.parser.structures.MethodStructure;
import me.cometkaizo.wavetech.parser.structures.VariableDeclaration;

public class DuplicateVariableDiagnostic extends ErrorDiagnostic {
    public DuplicateVariableDiagnostic(VariableDeclaration offendingVar, VariableDeclaration existingVar) {
        super(createMessage(offendingVar, existingVar), offendingVar.startPosition, offendingVar.endPosition);
    }
    public DuplicateVariableDiagnostic(VariableDeclaration offendingVar, MethodStructure.Parameter existingVar) {
        super(createMessage(offendingVar, existingVar), offendingVar.startPosition, offendingVar.endPosition);
    }

    private static String createMessage(VariableDeclaration offendingVar, VariableDeclaration existingVar) {
        String name = offendingVar.name.name;
        Position existingVarPosition = existingVar.startPosition;
        return "Variable '" + name + "' is already declared at " + existingVarPosition.line() + ", " + existingVarPosition.column();
    }

    private static String createMessage(VariableDeclaration offendingVar, MethodStructure.Parameter existingVar) {
        String name = offendingVar.name.name;
        Position existingVarPosition = existingVar.startPosition;
        return "Variable '" + name + "' is already declared as a parameter at " + existingVarPosition.line() + ", " + existingVarPosition.column();
    }
}
