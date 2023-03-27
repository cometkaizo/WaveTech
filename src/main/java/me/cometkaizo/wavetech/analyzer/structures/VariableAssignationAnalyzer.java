package me.cometkaizo.wavetech.analyzer.structures;

import me.cometkaizo.wavetech.analyzer.MethodResourcePool;
import me.cometkaizo.wavetech.analyzer.ResourcePool;
import me.cometkaizo.wavetech.analyzer.diagnostics.Diagnostic;
import me.cometkaizo.wavetech.analyzer.diagnostics.IncompatibleTypesDiagnostic;
import me.cometkaizo.wavetech.parser.structures.ClassStructure;
import me.cometkaizo.wavetech.parser.structures.EvaluableResource;
import me.cometkaizo.wavetech.parser.structures.VariableAssignation;

import java.util.List;

public class VariableAssignationAnalyzer implements MethodContextAnalyzer, ProgramContextAnalyzer {
    private final VariableAssignation assignation;
    boolean analyzed = false;

    public VariableAssignationAnalyzer(VariableAssignation assignation) {
        this.assignation = assignation;
    }

    @Override
    public void analyze(List<Diagnostic> problems, ResourcePool resources) {
        if (analyzed) return;
        analyzed = true;

        assignation.variableAccessor.getAnalyzer().analyze(problems, resources);
        assignation.value.getAnalyzer().analyze(problems, resources);

        var variable = (EvaluableResource) resources.getResourceOrAnalyze(assignation.variableAccessor, problems);
        if (variable == null) return;

        var expectedType = variable.getTypeOrAnalyze(resources, problems);
        if (expectedType == null) return;

        assignation.value.getAnalyzer().analyze(problems, resources);
        ClassStructure actualType = assignation.value.type;

        if (actualType != null && !expectedType.equals(actualType))
            problems.add(new IncompatibleTypesDiagnostic(actualType, expectedType, assignation.value));


    }

    @Override
    public void analyze(List<Diagnostic> problems, MethodResourcePool resources) {
        analyze(problems, (ResourcePool) resources);
    }
}
