package me.cometkaizo.wavetech.parser.structures;

import me.cometkaizo.wavetech.analyzer.structures.ExpressionAnalyzer;
import me.cometkaizo.wavetech.analyzer.structures.ProgramContextAnalyzer;
import me.cometkaizo.wavetech.lexer.tokens.Token;
import me.cometkaizo.wavetech.lexer.tokens.types.OperatorKeyword;
import me.cometkaizo.wavetech.syntaxes.AbstractSyntaxStructure;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Expression extends AbstractSyntaxStructure<ProgramContextAnalyzer> {

    public List<Term> terms = new ArrayList<>(2);
    public List<OperatorKeyword> operators = new ArrayList<>(1);
    public ClassStructure type;

    @Override
    public void store(String label, Object object) throws IllegalArgumentException {
        switch (label) {
            case "term" -> terms.add((Term) object);
            case "operator" -> operators.add((OperatorKeyword) ((Token) object).getType());
        }
    }

    public @NotNull ProgramContextAnalyzer createAnalyzer() {
        return new ExpressionAnalyzer(this);
    }

    /**
     * Gets all used variable identifiers. May include non-explicit field accesses.
     * @return all used variable identifiers in this structure.
     */
    public List<ResourceAccessor<?>> getUsedResources() {
        List<ResourceAccessor<?>> usedVariableNames = new ArrayList<>(2);

        for (var term : terms) {
            usedVariableNames.addAll(term.getUsedVariableNames());
        }

        return usedVariableNames;
    }

    @Override
    public String toString() {
        return "Expression{" +
                "terms=" + terms +
                ", operators=" + operators +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Expression that = (Expression) o;
        return Objects.equals(terms, that.terms) && Objects.equals(operators, that.operators);
    }

    @Override
    public int hashCode() {
        return Objects.hash(terms, operators);
    }

}
