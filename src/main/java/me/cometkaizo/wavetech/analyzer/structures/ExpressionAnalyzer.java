package me.cometkaizo.wavetech.analyzer.structures;

import me.cometkaizo.wavetech.analyzer.ResourcePool;
import me.cometkaizo.wavetech.analyzer.diagnostics.Diagnostic;
import me.cometkaizo.wavetech.analyzer.diagnostics.InapplicableOperatorDiagnostic;
import me.cometkaizo.wavetech.lexer.tokens.types.OperatorKeyword;
import me.cometkaizo.wavetech.parser.BuiltInClasses;
import me.cometkaizo.wavetech.parser.structures.ClassStructure;
import me.cometkaizo.wavetech.parser.structures.Expression;
import me.cometkaizo.wavetech.parser.structures.Term;

import java.util.List;

public class ExpressionAnalyzer implements ProgramContextAnalyzer {
    boolean analyzed = false;
    private final Expression expression;

    public ExpressionAnalyzer(Expression expression) {
        this.expression = expression;
    }

    @Override
    public void analyze(List<Diagnostic> problems, ResourcePool resources) {
        if (analyzed) return;

        Term term1 = expression.terms.get(0);
        term1.getAnalyzer().analyze(problems, resources);
        if (term1.type == null) return;
        ClassStructure lastType = term1.type;

        for (int index = 1; index < expression.terms.size(); index++) {
            OperatorKeyword operator = expression.operators.get(index - 1);
            Term term = expression.terms.get(index);

            term.getAnalyzer().analyze(problems, resources);
            if (term.type == null) return;
            ClassStructure type = term.type;

            var newType = getResultType(operator, lastType, type);

            if (newType == null) {
                problems.add(new InapplicableOperatorDiagnostic(operator, lastType, type, expression));
                return;
            }
            lastType = newType;
        }

        expression.type = lastType;
        analyzed = true;
    }

    private ClassStructure getResultType(OperatorKeyword operator, ClassStructure leftType, ClassStructure rightType) {
        return switch (operator) {
            case PLUS, MINUS -> leftType == BuiltInClasses.INT && rightType == BuiltInClasses.INT ? BuiltInClasses.INT : null;
            case DOUBLE_AMPERSAND, DOUBLE_PIPE -> leftType == BuiltInClasses.BOOLEAN && rightType == BuiltInClasses.BOOLEAN ? BuiltInClasses.BOOLEAN : null;
            default -> null;
        };
    }
}
