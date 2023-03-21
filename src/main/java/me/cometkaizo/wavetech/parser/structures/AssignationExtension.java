package me.cometkaizo.wavetech.parser.structures;

import me.cometkaizo.wavetech.analyzer.ResourcePool;
import me.cometkaizo.wavetech.analyzer.diagnostics.Diagnostic;
import me.cometkaizo.wavetech.analyzer.structures.ProgramContextAnalyzer;
import me.cometkaizo.wavetech.lexer.tokens.Token;
import me.cometkaizo.wavetech.lexer.tokens.types.OperatorKeyword;
import me.cometkaizo.wavetech.syntaxes.AbstractSyntaxStructure;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public class AssignationExtension extends AbstractSyntaxStructure<ProgramContextAnalyzer> {

    public OperatorKeyword operation;
    public Expression value;

    @Override
    public void store(String label, Object object) throws IllegalArgumentException {
        switch (label) {
            case "operation" -> operation = (OperatorKeyword) ((Token) object).getType();
            case "value" -> value = (Expression) object;
        }
    }

    @Override
    @NotNull
    protected ProgramContextAnalyzer createAnalyzer() {
        return new ProgramContextAnalyzer() {
            boolean analyzed = false;
            @Override
            public void analyze(List<Diagnostic> problems, ResourcePool resources) {
                if (analyzed) return;
                value.getAnalyzer().analyze(problems, resources);
                analyzed = true;
            }
        };
    }

    @Override
    public String toString() {
        return "AssignationExtension{" +
                "operation=" + operation +
                ", value=" + value +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AssignationExtension that = (AssignationExtension) o;
        return operation == that.operation && Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(operation, value);
    }
}
