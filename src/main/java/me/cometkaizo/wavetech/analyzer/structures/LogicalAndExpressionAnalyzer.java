package me.cometkaizo.wavetech.analyzer.structures;

import me.cometkaizo.wavetech.analyzer.ResourcePool;
import me.cometkaizo.wavetech.analyzer.diagnostics.Diagnostic;
import me.cometkaizo.wavetech.analyzer.diagnostics.InapplicableOperatorDiagnostic;
import me.cometkaizo.wavetech.lexer.tokens.types.OperatorKeyword;
import me.cometkaizo.wavetech.parser.structures.BitwiseOrExpression;
import me.cometkaizo.wavetech.parser.structures.ClassStructure;
import me.cometkaizo.wavetech.parser.structures.LogicalAndExpression;

import java.util.List;

import static me.cometkaizo.wavetech.parser.BuiltInClasses.BOOLEAN;

public class LogicalAndExpressionAnalyzer implements ProgramContextAnalyzer {
    private final LogicalAndExpression expression;
    boolean analyzed = false;

    public LogicalAndExpressionAnalyzer(LogicalAndExpression expression) {
        this.expression = expression;
    }


    @Override
    public void analyze(List<Diagnostic> problems, ResourcePool resources) {
        if (analyzed) return;
        analyzed = true;
        throwIfIllegalState();

        ClassStructure lastType;

        var firstExpr = getFirstExpr();
        analyze(problems, resources, firstExpr);
        if (firstExpr.type == null) return;
        lastType = firstExpr.type;

        for (int exprIndex = 1; exprIndex < expression.expressions.size(); exprIndex ++) {
            var expr = expression.expressions.get(exprIndex);
            var operator = expression.operators.get(exprIndex - 1);

            analyze(problems, resources, expr);
            if (expr.type == null) return;

            var newType = getResultType(lastType, expr.type);

            if (newType == null) {
                addInapplicableOperatorDiagnostic(problems, lastType, expr.type, operator);
                return;
            } else lastType = newType;
        }

        expression.type = lastType;
    }

    private void addInapplicableOperatorDiagnostic(List<Diagnostic> problems, ClassStructure leftType, ClassStructure rightType, OperatorKeyword operator) {
        problems.add(new InapplicableOperatorDiagnostic(operator, expression, leftType, rightType));
    }

    private static void analyze(List<Diagnostic> problems, ResourcePool resources, BitwiseOrExpression expr) {
        expr.getAnalyzer().analyze(problems, resources);
    }

    private BitwiseOrExpression getFirstExpr() {
        return expression.expressions.get(0);
    }

    private void throwIfIllegalState() {
        if (expression.expressions == null || expression.expressions.isEmpty())
            throw new IllegalStateException("Expression list is null or empty");
        if (expression.operators == null)
            throw new IllegalStateException("Operators list is null");
        if (expression.operators.size() != expression.expressions.size() - 1)
            throw new IllegalStateException("Length mismatch; number of operators (" + expression.operators.size() + ") " +
                    "is not one less than number of expressions (" + expression.expressions.size() + ")" +
                    "\nOperators: " + expression.operators +
                    "\nExpressions: " + expression.expressions);
    }

    private static ClassStructure getResultType(ClassStructure leftType, ClassStructure rightType) {
        return leftType == BOOLEAN && rightType == BOOLEAN ? BOOLEAN : null;
    }

}
