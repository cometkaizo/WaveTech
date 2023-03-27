package me.cometkaizo.wavetech.analyzer.structures;

import me.cometkaizo.wavetech.analyzer.ResourcePool;
import me.cometkaizo.wavetech.analyzer.diagnostics.Diagnostic;
import me.cometkaizo.wavetech.analyzer.diagnostics.InapplicableOperatorDiagnostic;
import me.cometkaizo.wavetech.lexer.tokens.types.OperatorKeyword;
import me.cometkaizo.wavetech.parser.structures.ClassStructure;
import me.cometkaizo.wavetech.parser.structures.Factor;
import me.cometkaizo.wavetech.parser.structures.PostfixExpression;

import java.util.List;

import static me.cometkaizo.wavetech.parser.BuiltInClasses.INT;

public class PostfixExpressionAnalyzer implements ProgramContextAnalyzer {
    private final PostfixExpression expression;
    boolean analyzed = false;

    public PostfixExpressionAnalyzer(PostfixExpression expression) {
        this.expression = expression;
    }

    @Override
    public void analyze(List<Diagnostic> problems, ResourcePool resources) {
        if (analyzed) return;
        analyzed = true;
        throwIfIllegalState();

        ClassStructure lastType;

        var expr = expression.expression;
        analyze(problems, resources, expr);
        if (expr.type == null) return;
        lastType = expr.type;

        for (var operator : expression.operators) {
            var newType = getResultType(operator, lastType);

            if (newType == null) {
                addInapplicableOperatorDiagnostic(problems, lastType, operator);
                return;
            } else lastType = newType;
        }

        expression.type = lastType;
    }

    private void addInapplicableOperatorDiagnostic(List<Diagnostic> problems, ClassStructure argType, OperatorKeyword operator) {
        problems.add(new InapplicableOperatorDiagnostic(operator, expression, argType));
    }

    private static void analyze(List<Diagnostic> problems, ResourcePool resources, Factor expr) {
        expr.getAnalyzer().analyze(problems, resources);
    }

    private void throwIfIllegalState() {
        if (expression.expression == null)
            throw new IllegalStateException("Expression is null");
        if (expression.operators == null)
            throw new IllegalStateException("Operators list is null");
    }

    private static ClassStructure getResultType(OperatorKeyword operator, ClassStructure argType) {
        return switch (operator) {
            case DOUBLE_PLUS, DOUBLE_MINUS -> argType == INT ? INT : null;
            default -> null;
        };
    }

}
