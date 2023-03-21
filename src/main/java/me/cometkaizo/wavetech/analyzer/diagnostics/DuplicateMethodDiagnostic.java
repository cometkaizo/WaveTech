package me.cometkaizo.wavetech.analyzer.diagnostics;

import me.cometkaizo.wavetech.parser.structures.ClassStructure;
import me.cometkaizo.wavetech.parser.structures.MethodStructure;

import java.util.stream.Collectors;

public class DuplicateMethodDiagnostic extends ErrorDiagnostic {
    public DuplicateMethodDiagnostic(MethodStructure offendingMethod, ClassStructure clazz) {
        super(createMessage(offendingMethod, clazz), offendingMethod.startPosition, offendingMethod.endPosition);
    }

    private static String createMessage(MethodStructure offendingMethod, ClassStructure clazz) {
        String name = offendingMethod.name.name;
        String parameterTypes = offendingMethod.parameters.stream().map(p -> p.typeAccessor.getSignature().name).collect(Collectors.joining(", "));
        String className = clazz.name.name;

        return "Method '" + name + '(' + parameterTypes + ")' is already defined in '" + className + "'";
    }
}
