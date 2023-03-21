package me.cometkaizo.wavetech.analyzer.diagnostics;

import me.cometkaizo.wavetech.parser.structures.MethodAccessor;

import java.util.stream.Collectors;

public class UnknownMethodDiagnostic extends ErrorDiagnostic {
    public UnknownMethodDiagnostic(MethodAccessor accessor) {
        super(createMessage(accessor), accessor.startPosition, accessor.endPosition);
    }

    private static String createMessage(MethodAccessor accessor) {
        var parameterTypes = accessor.getSignature().parameterTypes().stream().map(c -> {
            if (c != null) return c.name.name;
            return "[Unknown]";
        }).collect(Collectors.joining(", "));
        var name = accessor.methodName.name;

        return "Cannot find method '" + name + '(' + parameterTypes + ")'";
    }
}
