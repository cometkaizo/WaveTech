package me.cometkaizo.wavetech.analyzer.diagnostics;

import me.cometkaizo.wavetech.parser.structures.FieldDeclaration;
import me.cometkaizo.wavetech.parser.structures.Identifier;
import me.cometkaizo.wavetech.parser.structures.MethodStructure;
import me.cometkaizo.wavetech.parser.structures.VariableDeclaration;

public class ReferenceFromStaticContextDiagnostic extends ErrorDiagnostic {
    public ReferenceFromStaticContextDiagnostic(Identifier referencedVarName, FieldDeclaration field) {
        super(createMessage(referencedVarName, field.name), referencedVarName.startPosition, referencedVarName.endPosition);
    }
    public ReferenceFromStaticContextDiagnostic(Identifier referencedVarName, VariableDeclaration var) {
        super(createMessage(referencedVarName, var.name), referencedVarName.startPosition, referencedVarName.endPosition);
    }
    public ReferenceFromStaticContextDiagnostic(Identifier referencedVarName, MethodStructure method) {
        super(createMessage(referencedVarName, method.name), referencedVarName.startPosition, referencedVarName.endPosition);
    }

    private static String createMessage(Identifier referencedVarName, Identifier from) {
        return "Variable '" + referencedVarName.name + "' cannot be referenced from static context '" + from.name + "'";
    }
}
