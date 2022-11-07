package me.cometkaizo.temp.nodes;

import me.cometkaizo.wavetech.lexer.tokens.Token;
import me.cometkaizo.wavetech.lexer.tokens.types.Keywords;
import me.cometkaizo.wavetech.lexer.tokens.types.PrimitiveOperator;
import me.cometkaizo.wavetech.lexer.tokens.types.PrimitiveValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static me.cometkaizo.wavetech.lexer.tokens.types.ObjectType.REFERENCE;

public class UnaryOperationNode extends OperationNode {

    @Nullable
    private final PrimitiveOperator operator;
    @NotNull
    private final Token value;

    public UnaryOperationNode(@Nullable PrimitiveOperator operator, @NotNull Token operand) {
        this.operator = operator;
        this.value = operand;

        throwIfInvalidOperator(operator);
        throwIfInvalidValue(operand);
    }

    public UnaryOperationNode(@NotNull Token value) {
        this(null, value);
    }

    private void throwIfInvalidValue(Token value) {
        if (value.getType() instanceof PrimitiveValue) return;
        if (value.getType() == REFERENCE) return;
        throw new IllegalArgumentException("Illegal value for unary operator: " + value);
    }

    private void throwIfInvalidOperator(PrimitiveOperator operator) {
        if (operator != null && !Keywords.isUnaryOperator(operator))
            throw new IllegalArgumentException("Illegal unary operator: " + operator);
    }

    @Override
    public String toString() {
        return "UnaryOperationNode{" +
                "operator=" + operator +
                ", value=" + value +
                '}';
    }
}
