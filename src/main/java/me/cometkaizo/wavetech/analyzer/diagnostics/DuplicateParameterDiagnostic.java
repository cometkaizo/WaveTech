package me.cometkaizo.wavetech.analyzer.diagnostics;

import me.cometkaizo.wavetech.lexer.tokens.Position;
import me.cometkaizo.wavetech.parser.structures.MethodStructure;

public class DuplicateParameterDiagnostic extends ErrorDiagnostic {
    public DuplicateParameterDiagnostic(MethodStructure.Parameter offendingParam, MethodStructure.Parameter existingParam, MethodStructure method) {
        super(createMessage(offendingParam, existingParam, method), offendingParam.startPosition, offendingParam.endPosition);
    }

    private static String createMessage(MethodStructure.Parameter offendingParam, MethodStructure.Parameter existingParam, MethodStructure method) {
        String name = offendingParam.name.name;
        String methodName = method.name.name;
        Position existingParamPos = existingParam.startPosition;
        return "Parameter '" + name + "' is already declared in '" + methodName + "' at " + existingParamPos.line() + ", " + existingParamPos.column();
    }
}
