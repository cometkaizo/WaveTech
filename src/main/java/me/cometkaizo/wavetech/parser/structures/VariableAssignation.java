package me.cometkaizo.wavetech.parser.structures;

import me.cometkaizo.wavetech.analyzer.structures.VariableAssignationAnalyzer;
import me.cometkaizo.wavetech.lexer.tokens.Token;
import me.cometkaizo.wavetech.lexer.tokens.types.OperatorKeyword;
import me.cometkaizo.wavetech.syntaxes.AbstractSyntaxStructure;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class VariableAssignation extends AbstractSyntaxStructure<VariableAssignationAnalyzer> implements MethodStatement {
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
    protected VariableAssignationAnalyzer createAnalyzer() {
        return new VariableAssignationAnalyzer(this);
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
