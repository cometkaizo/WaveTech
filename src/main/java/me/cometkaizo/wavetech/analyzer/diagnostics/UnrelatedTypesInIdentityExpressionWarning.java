package me.cometkaizo.wavetech.analyzer.diagnostics;

import me.cometkaizo.wavetech.parser.structures.ClassStructure;
import me.cometkaizo.wavetech.parser.structures.EqualityExpression;

public class UnrelatedTypesInIdentityExpressionWarning extends WarningDiagnostic {
    public UnrelatedTypesInIdentityExpressionWarning(ClassStructure leftType, ClassStructure rightType, EqualityExpression offendingStructure) {
        super(createMessage(leftType, rightType), offendingStructure.startPosition, offendingStructure.endPosition);
    }

    private static String createMessage(ClassStructure leftType, ClassStructure rightType) {
        String formattedLeft = leftType.name.name;
        String formattedRight = rightType.name.name;
        return "Types '" + formattedLeft + "' and '" + formattedRight + "' are unrelated; expression is always false";
    }
}
