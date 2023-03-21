package me.cometkaizo.wavetech.analyzer.structures;

import me.cometkaizo.wavetech.analyzer.MethodResourcePool;
import me.cometkaizo.wavetech.analyzer.ResourcePool;
import me.cometkaizo.wavetech.analyzer.diagnostics.Diagnostic;
import me.cometkaizo.wavetech.analyzer.diagnostics.NoReturnTypeDiagnostic;
import me.cometkaizo.wavetech.parser.structures.ClassStructure;
import me.cometkaizo.wavetech.parser.structures.Factor;
import me.cometkaizo.wavetech.parser.structures.MethodAccessor;
import me.cometkaizo.wavetech.parser.structures.MethodStructure;

import java.util.ArrayList;
import java.util.List;

public class MethodAccessorAnalyzer extends ResourceAnalyzer implements MethodContextAnalyzer {
    boolean analyzed = false;
    private final MethodAccessor methodAccessor;

    public MethodAccessorAnalyzer(MethodAccessor methodAccessor) {
        super(methodAccessor);
        this.methodAccessor = methodAccessor;
    }

    public void analyze(List<Diagnostic> problems, ResourcePool resources) {
        if (analyzed) return;
        analyzed = true;

        methodAccessor.methodName.getAnalyzer().analyze(problems);

        setSignature(problems, resources);

        var accessedMethod = (MethodStructure) resources.getResourceOrAnalyze(methodAccessor, problems);
        if (accessedMethod == null) {
            problems.add(methodAccessor.getNoResourceDiagnostic());
        } else if (methodAccessor.parent instanceof Factor && accessedMethod.returnType == null) {
            problems.add(new NoReturnTypeDiagnostic(accessedMethod, methodAccessor));
        }

    }

    private void setSignature(List<Diagnostic> problems, ResourcePool resources) {
        List<ClassStructure> parameterTypes = new ArrayList<>(methodAccessor.parameters.size());

        for (var expression : methodAccessor.parameters) {
            expression.getAnalyzer().analyze(problems, resources);
            parameterTypes.add(expression.type);
        }
        methodAccessor.signature = new MethodStructure.Signature(methodAccessor.methodName, parameterTypes);
    }

    @Override
    public void analyze(List<Diagnostic> problems, MethodResourcePool resources) {
        analyze(problems, (ResourcePool) resources);
    }
}
