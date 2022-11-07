package me.cometkaizo.wavetech.parser.nodes;

import me.cometkaizo.wavetech.lexer.tokens.types.Keywords;
import me.cometkaizo.wavetech.lexer.tokens.types.PrimitiveOperator;

public class BinaryOperationNode extends OperationNode {

    private final PrimitiveOperator operator;
    private final OperationNode left;
    private final OperationNode right;

    public BinaryOperationNode(PrimitiveOperator operator, OperationNode left, OperationNode right) {
        this.operator = operator;
        this.left = left;
        this.right = right;

        throwIfInvalidOperator(operator);
    }

    private void throwIfInvalidOperator(PrimitiveOperator operator) {
        if (!Keywords.isBinaryOperator(operator))
            throw new IllegalArgumentException("Illegal binary operator: " + operator);
    }

    @Override
    public String toString() {
        return "BinaryOperationNode{\n" +
                "    operator=" + operator +
                ", \n    left=\n" + left.toString().indent(8) +
                "    right=\n" + right.toString().indent(8) +
                "}";
    }
}
