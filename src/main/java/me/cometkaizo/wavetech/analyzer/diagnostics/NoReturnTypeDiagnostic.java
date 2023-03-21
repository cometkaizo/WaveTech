package me.cometkaizo.wavetech.analyzer.diagnostics;

import me.cometkaizo.wavetech.parser.structures.MethodAccessor;
import me.cometkaizo.wavetech.parser.structures.MethodStructure;

import java.util.stream.Collectors;

public class NoReturnTypeDiagnostic extends ErrorDiagnostic {
    public NoReturnTypeDiagnostic(MethodStructure offendingMethod, MethodAccessor accessor) {
        super(createMessage(offendingMethod), accessor.startPosition, accessor.endPosition);
    }

    private static String createMessage(MethodStructure offendingMethod) {
        String name = offendingMethod.name.name;
        String parameterTypes = offendingMethod.parameters.stream().map(p -> p.typeAccessor.getSignature().name).collect(Collectors.joining(", "));

        return "Method '" + name + '(' + parameterTypes + ")' does not return a value; cannot be used in expression";
    }
}
