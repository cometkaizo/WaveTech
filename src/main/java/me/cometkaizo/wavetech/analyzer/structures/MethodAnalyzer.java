package me.cometkaizo.wavetech.analyzer.structures;

import me.cometkaizo.wavetech.analyzer.ClassResourcePool;
import me.cometkaizo.wavetech.analyzer.MethodResourcePool;
import me.cometkaizo.wavetech.analyzer.diagnostics.AbstractMethodDiagnostic;
import me.cometkaizo.wavetech.analyzer.diagnostics.Diagnostic;
import me.cometkaizo.wavetech.analyzer.diagnostics.DuplicateMethodDiagnostic;
import me.cometkaizo.wavetech.analyzer.diagnostics.DuplicateParameterDiagnostic;
import me.cometkaizo.wavetech.parser.structures.ClassStructure;
import me.cometkaizo.wavetech.parser.structures.MethodStructure;

import java.util.ArrayList;
import java.util.List;

public class MethodAnalyzer implements ClassContextAnalyzer {
    boolean analyzed = false;
    private final MethodStructure method;

    public MethodAnalyzer(MethodStructure method) {
        this.method = method;
    }

    private void throwIfInvalidState() {
        if (method.visibility == null) throw new IllegalStateException("Visibility is null");
        if (method.name == null) throw new IllegalStateException("Name is null");
        if (method.body == null) throw new IllegalStateException("Body is null");
        if (method.body.statements == null) throw new IllegalStateException("Statements are null");
    }

    @Override
    public void analyze(List<Diagnostic> problems, ClassResourcePool resources) {
        if (analyzed) return;
        analyzed = true;
        throwIfInvalidState();

        checkReturnType(problems, resources);
        if (method.isAbstract() && !method.parent.isAbstract())
            problems.add(new AbstractMethodDiagnostic(method, method.parent));

        List<ClassStructure> parameterTypes = new ArrayList<>(method.parameters.size());
        for (var parameter : method.parameters) {
            var parameterTypeAccessor = parameter.typeAccessor;
            parameterTypeAccessor.getAnalyzer().analyze(problems, resources);

            var parameterType = (ClassStructure) resources.getResourceOrAnalyze(parameterTypeAccessor, problems);
            if (parameterType == null) return;

            parameterTypes.add(parameterType);
        }
        method.signature = new MethodStructure.Signature(method.name, parameterTypes);

        checkDuplicateMethods(problems);
        checkDuplicateParameters(problems);
        var methodResources = new MethodResourcePool(resources, method.parameters, new ArrayList<>(2));

        method.body.getAnalyzer().analyze(problems, methodResources);

    }

    private void checkDuplicateMethods(List<Diagnostic> problems) {
        for (var otherMethod : method.parent.methods) {
            if (method == otherMethod) continue;
            if (method.signature.equals(otherMethod.signature))
                problems.add(new DuplicateMethodDiagnostic(method, method.parent));
        }
    }

    private void checkReturnType(List<Diagnostic> problems, ClassResourcePool resources) {
        if (method.returnType != null)
            method.returnType.getAnalyzer().analyze(problems, resources);
    }

    private void checkDuplicateParameters(List<Diagnostic> problems) {
        var parameters = method.parameters;

        for (int index1 = 0; index1 < parameters.size(); index1 ++) {
            var parameter1 = parameters.get(index1);

            for (int index2 = index1 + 1; index2 < parameters.size(); index2 ++) {
                var parameter2 = parameters.get(index2);

                if (parameter1.name.equals(parameter2.name))
                    problems.add(new DuplicateParameterDiagnostic(parameter2, parameter1, method));
            }
        }
    }
}
