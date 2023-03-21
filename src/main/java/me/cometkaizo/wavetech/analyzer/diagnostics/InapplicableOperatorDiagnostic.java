package me.cometkaizo.wavetech.analyzer.diagnostics;

import me.cometkaizo.wavetech.lexer.tokens.types.OperatorKeyword;
import me.cometkaizo.wavetech.parser.structures.ClassStructure;
import me.cometkaizo.wavetech.syntaxes.SyntaxStructure;

public class InapplicableOperatorDiagnostic extends ErrorDiagnostic {


    public InapplicableOperatorDiagnostic(OperatorKeyword operator, ClassStructure leftType, ClassStructure rightType, SyntaxStructure offendingStructure) {
        super(createMessage(operator, leftType, rightType), offendingStructure.getStartPosition(), offendingStructure.getEndPosition());
    }

    private static String createMessage(OperatorKeyword operator, ClassStructure leftType, ClassStructure rightType) {
        return "Operator '" + operator.symbol() + "' cannot be applied to '" + leftType.name.name + "' and '" + rightType.name.name + '\'';
    }
}
