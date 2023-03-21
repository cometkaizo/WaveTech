package me.cometkaizo.wavetech.analyzer.structures;

import me.cometkaizo.wavetech.analyzer.MethodResourcePool;
import me.cometkaizo.wavetech.analyzer.diagnostics.Diagnostic;
import me.cometkaizo.wavetech.analyzer.diagnostics.DuplicateVariableDiagnostic;
import me.cometkaizo.wavetech.analyzer.diagnostics.IncompatibleTypesDiagnostic;
import me.cometkaizo.wavetech.parser.structures.ClassStructure;
import me.cometkaizo.wavetech.parser.structures.VariableDeclaration;

import java.util.List;

public class VariableDeclarationAnalyzer implements MethodContextAnalyzer {
    private final VariableDeclaration variableDeclaration;
    boolean analyzed = false;

    public VariableDeclarationAnalyzer(VariableDeclaration variableDeclaration) {
        this.variableDeclaration = variableDeclaration;
    }

    @Override
    public void analyze(List<Diagnostic> problems, MethodResourcePool resources) {
        if (analyzed) return;
        analyzed = true;
        throwIfInvalidState();

        variableDeclaration.typeAccessor.getAnalyzer().analyze(problems, resources);
        variableDeclaration.name.getAnalyzer().analyze(problems);

        checkIncompatibleInitializer(problems, resources);
        checkDuplicateDeclaration(problems, resources);

    }

    private void checkDuplicateDeclaration(List<Diagnostic> problems, MethodResourcePool resources) {
        var alreadyDeclaredVariable = resources.getVariable(variableDeclaration.name, problems);

        if (alreadyDeclaredVariable != null && !variableDeclaration.equals(alreadyDeclaredVariable)) {
            problems.add(new DuplicateVariableDiagnostic(variableDeclaration, alreadyDeclaredVariable));
        } else {
            var alreadyDeclaredParameter = resources.getParameter(variableDeclaration.name, problems);
            if (alreadyDeclaredParameter != null) {
                problems.add(new DuplicateVariableDiagnostic(variableDeclaration, alreadyDeclaredParameter));
            } else {
                resources.variables.add(variableDeclaration);
            }
        }
    }

    private void checkIncompatibleInitializer(List<Diagnostic> problems, MethodResourcePool resources) {
        var expectedType = (ClassStructure) resources.getResourceOrAnalyze(variableDeclaration.typeAccessor, problems);
        if (variableDeclaration.initializer != null && expectedType != null) {
            variableDeclaration.initializer.getAnalyzer().analyze(problems, resources);
            ClassStructure actualType = variableDeclaration.initializer.value.type;

            if (actualType != null && !expectedType.equals(actualType))
                problems.add(new IncompatibleTypesDiagnostic(actualType, expectedType, variableDeclaration.initializer.value));
        }
    }

    private void throwIfInvalidState() {
        if (variableDeclaration.name == null) throw new IllegalStateException("Name is null");
        if (variableDeclaration.typeAccessor == null) throw new IllegalStateException("Type is null");
    }
}
