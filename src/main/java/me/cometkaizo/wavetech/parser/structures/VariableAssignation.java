package me.cometkaizo.wavetech.parser.structures;

import me.cometkaizo.wavetech.analyzer.MethodResourcePool;
import me.cometkaizo.wavetech.analyzer.diagnostics.Diagnostic;
import me.cometkaizo.wavetech.analyzer.diagnostics.IncompatibleTypesDiagnostic;
import me.cometkaizo.wavetech.analyzer.structures.MethodContextAnalyzer;
import me.cometkaizo.wavetech.lexer.tokens.Token;
import me.cometkaizo.wavetech.lexer.tokens.types.OperatorKeyword;
import me.cometkaizo.wavetech.syntaxes.AbstractSyntaxStructure;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public class VariableAssignation extends AbstractSyntaxStructure<MethodContextAnalyzer> implements MethodStatement {
    public VariableAccessor variableAccessor;
    public OperatorKeyword operation;
    public Expression value;

    @Override
    public void store(String label, Object object) throws IllegalArgumentException {
        switch (label) {
            case "variableAccessor" -> variableAccessor = (VariableAccessor) object;
            case "operation" -> operation = (OperatorKeyword) ((Token) object).getType();
            case "value" -> value = (Expression) object;
        }
    }

    @Override
    @NotNull
    protected MethodContextAnalyzer createAnalyzer() {
        return new MethodContextAnalyzer() {
            boolean analyzed = false;
            @Override
            public void analyze(List<Diagnostic> problems, MethodResourcePool resources) {
                if (analyzed) return;
                analyzed = true;

                variableAccessor.getAnalyzer().analyze(problems, resources);
                value.getAnalyzer().analyze(problems, resources);

                var variable = (EvaluableResource) resources.getResourceOrAnalyze(variableAccessor, problems);
                if (variable == null) return;

                var expectedType = variable.getTypeOrAnalyze(resources, problems);
                if (expectedType == null) return;

                value.getAnalyzer().analyze(problems, resources);
                ClassStructure actualType = value.type;

                if (actualType != null && !expectedType.equals(actualType))
                    problems.add(new IncompatibleTypesDiagnostic(actualType, expectedType, value));


            }
        };
    }

    @Override
    public String toString() {
        return "VariableAssignationStatement{" +
                "name=" + variableAccessor +
                ", operation=" + operation +
                ", value=" + value +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VariableAssignation that = (VariableAssignation) o;
        return Objects.equals(variableAccessor, that.variableAccessor) && operation == that.operation && Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(variableAccessor, operation, value);
    }
}
