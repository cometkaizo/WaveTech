package me.cometkaizo.wavetech.analyzer.diagnostics;

import me.cometkaizo.wavetech.parser.structures.ClassStructure;
import me.cometkaizo.wavetech.parser.structures.MethodStructure;

import java.util.stream.Collectors;

public class AbstractMethodDiagnostic extends ErrorDiagnostic {

    public AbstractMethodDiagnostic(MethodStructure method, ClassStructure clazz) {
        super(createMessage(method, clazz), method.startPosition, method.endPosition);
    }

    private static String createMessage(MethodStructure method, ClassStructure clazz) {
        String name = method.name.name;
        String parameterTypes = method.parameters.stream().map(p -> p.typeAccessor.className.name).collect(Collectors.joining(", "));
        String className = clazz.name.name;

        return "Abstract method '" + name + '(' + parameterTypes + ")' is declared in non-abstract class '" + className + "'";
    }

}
