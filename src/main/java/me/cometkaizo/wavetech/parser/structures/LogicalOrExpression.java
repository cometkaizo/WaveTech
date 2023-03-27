package me.cometkaizo.wavetech.parser.structures;

import me.cometkaizo.wavetech.analyzer.structures.LogicalOrExpressionAnalyzer;
import me.cometkaizo.wavetech.analyzer.structures.ProgramContextAnalyzer;
import me.cometkaizo.wavetech.lexer.tokens.Token;
import me.cometkaizo.wavetech.lexer.tokens.types.OperatorKeyword;
import me.cometkaizo.wavetech.syntaxes.AbstractSyntaxStructure;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LogicalOrExpression extends AbstractSyntaxStructure<ProgramContextAnalyzer> {

    public List<LogicalAndExpression> expressions = new ArrayList<>(2);
    public List<OperatorKeyword> operators = new ArrayList<>(1);

    public ClassStructure type;

    @Override
    public void store(String label, Object object) throws IllegalArgumentException {
        switch (label) {
            case "expression" -> expressions.add((LogicalAndExpression) object);
            case "operator" -> operators.add((OperatorKeyword) ((Token) object).getType());
        }
    }

    public @NotNull ProgramContextAnalyzer createAnalyzer() {
        return new LogicalOrExpressionAnalyzer(this);
    }

    @Override
    public String toString() {
        return "LogicalOrExpression{" +
                "expressions=" + expressions +
                ", operators=" + operators +
                ", type=" + type +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LogicalOrExpression that = (LogicalOrExpression) o;
        return Objects.equals(expressions, that.expressions) && Objects.equals(operators, that.operators) && Objects.equals(type, that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(expressions, operators, type);
    }
}
