package me.cometkaizo.wavetech.parser.structures;

import me.cometkaizo.wavetech.analyzer.structures.ExpressionAnalyzer;
import me.cometkaizo.wavetech.analyzer.structures.ProgramContextAnalyzer;
import me.cometkaizo.wavetech.syntaxes.AbstractSyntaxStructure;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class Expression extends AbstractSyntaxStructure<ProgramContextAnalyzer> {

    // exactly one of these will be non-null
    public TernaryExpression ternary;
    public VariableAssignation varAssignment;

    // the type of this expression; null until this expression is analyzed
    public ClassStructure type;

    @Override
    public void store(String label, Object object) throws IllegalArgumentException {
        if (!"part".equals(label)) return;

        if (object instanceof TernaryExpression) {
            ternary = cast(object);
        } else if (object instanceof VariableAssignation) {
            varAssignment = cast(object);
        }
    }

    public @NotNull ProgramContextAnalyzer createAnalyzer() {
        return new ExpressionAnalyzer(this);
    }
/*
    /**
     * Gets all used variable identifiers. May include non-explicit field accesses.
     * @return all used variable identifiers in this structure.
     *
    public List<ResourceAccessor<?>> getUsedResources() {
        List<ResourceAccessor<?>> usedVariableNames = new ArrayList<>(2);

        for (var term : terms) {
            usedVariableNames.addAll(term.getUsedVariableNames());
        }

        return usedVariableNames;
    }*/

    @Override
    public String toString() {
        return "Expression{" +
                "ternary=" + ternary +
                ", assignment=" + varAssignment +
                ", type=" + type +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Expression that = (Expression) o;
        return Objects.equals(ternary, that.ternary) && Objects.equals(varAssignment, that.varAssignment) && Objects.equals(type, that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ternary, varAssignment, type);
    }
}
