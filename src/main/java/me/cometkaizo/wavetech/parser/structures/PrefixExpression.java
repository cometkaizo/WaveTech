package me.cometkaizo.wavetech.parser.structures;

import me.cometkaizo.wavetech.analyzer.structures.PrefixExpressionAnalyzer;
import me.cometkaizo.wavetech.analyzer.structures.ProgramContextAnalyzer;
import me.cometkaizo.wavetech.lexer.tokens.Token;
import me.cometkaizo.wavetech.lexer.tokens.types.OperatorKeyword;
import me.cometkaizo.wavetech.syntaxes.AbstractSyntaxStructure;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class PrefixExpression extends AbstractSyntaxStructure<ProgramContextAnalyzer> {

    public OperatorKeyword operator;
    public PrefixExpression argument;
    public PostfixExpression expression;

    public ClassStructure type;

    @Override
    public void store(String label, Object object) throws IllegalArgumentException {
        switch (label) {
            case "operator" -> operator = (OperatorKeyword) ((Token) object).getType();
            case "argument" -> argument = cast(object);
            case "expression" -> expression = cast(object);
        }
    }

    public @NotNull ProgramContextAnalyzer createAnalyzer() {
        return new PrefixExpressionAnalyzer(this);
    }

    @Override
    public String toString() {
        return "PrefixExpression{" +
                "operator=" + operator +
                ", argument=" + argument +
                ", expression=" + expression +
                ", type=" + type +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PrefixExpression that = (PrefixExpression) o;
        return operator == that.operator && Objects.equals(argument, that.argument) && Objects.equals(expression, that.expression) && Objects.equals(type, that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(operator, argument, expression, type);
    }
}
