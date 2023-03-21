package me.cometkaizo.wavetech.parser.structures;

import me.cometkaizo.wavetech.analyzer.structures.ProgramContextAnalyzer;
import me.cometkaizo.wavetech.analyzer.structures.TermAnalyzer;
import me.cometkaizo.wavetech.lexer.tokens.Token;
import me.cometkaizo.wavetech.lexer.tokens.types.OperatorKeyword;
import me.cometkaizo.wavetech.syntaxes.AbstractSyntaxStructure;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Term extends AbstractSyntaxStructure<ProgramContextAnalyzer> {

    public List<Factor> factors = new ArrayList<>(2);
    public List<OperatorKeyword> operators = new ArrayList<>(1);

    public ClassStructure type;

    @Override
    public void store(String label, Object object) throws IllegalArgumentException {
        switch (label) {
            case "factor" -> factors.add((Factor) object);
            case "operator" -> operators.add((OperatorKeyword) ((Token) object).getType());
        }
    }

    public @NotNull ProgramContextAnalyzer createAnalyzer() {
        return new TermAnalyzer(this);
    }

    public List<ResourceAccessor<?>> getUsedVariableNames() {
        List<ResourceAccessor<?>> usedVariableNames = new ArrayList<>(1);

        for (var factor : factors) {
            if (factor.resourceAccessor != null) usedVariableNames.add(factor.resourceAccessor);
        }

        return usedVariableNames;
    }

    @Override
    public String toString() {
        return "Term{" +
                "factors=" + factors +
                ", operators=" + operators +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Term term = (Term) o;
        return Objects.equals(factors, term.factors) && Objects.equals(operators, term.operators);
    }

    @Override
    public int hashCode() {
        return Objects.hash(factors, operators);
    }

}
