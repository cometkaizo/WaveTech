package me.cometkaizo.wavetech.analyzer.structures;

import me.cometkaizo.wavetech.analyzer.ResourcePool;
import me.cometkaizo.wavetech.analyzer.diagnostics.Diagnostic;
import me.cometkaizo.wavetech.analyzer.diagnostics.InapplicableOperatorDiagnostic;
import me.cometkaizo.wavetech.lexer.tokens.types.OperatorKeyword;
import me.cometkaizo.wavetech.parser.structures.ClassStructure;
import me.cometkaizo.wavetech.parser.structures.PrefixExpression;
import me.cometkaizo.wavetech.syntaxes.AbstractSyntaxStructure;

import java.util.List;

import static me.cometkaizo.wavetech.parser.BuiltInClasses.BOOLEAN;
import static me.cometkaizo.wavetech.parser.BuiltInClasses.INT;

public class PrefixExpressionAnalyzer implements ProgramContextAnalyzer {
    private final PrefixExpression expression;
    boolean analyzed = false;

    public PrefixExpressionAnalyzer(PrefixExpression expression) {
        this.expression = expression;
    }

    @Override
    public void analyze(List<Diagnostic> problems, ResourcePool resources) {
        if (analyzed) return;
        analyzed = true;
        throwIfIllegalState();

        if (expression.expression != null) {
            analyze(problems, resources, expression.expression);
            expression.type = expression.expression.type;
            return;
        }

        var argument = expression.argument;
        var operator = expression.operator;

        analyze(problems, resources, argument);
        if (argument.type == null) return;

        var type = getResultType(operator, argument.type);

        if (type == null) addInapplicableOperatorDiagnostic(problems, argument.type, operator);
        else expression.type = type;
    }

    private void addInapplicableOperatorDiagnostic(List<Diagnostic> problems, ClassStructure argType, OperatorKeyword operator) {
        problems.add(new InapplicableOperatorDiagnostic(operator, expression, argType));
    }

    private static void analyze(List<Diagnostic> problems, ResourcePool resources, AbstractSyntaxStructure<? extends ProgramContextAnalyzer> expr) {
        expr.getAnalyzer().analyze(problems, resources);
    }

    private void throwIfIllegalState() {
        if (expression.argument == null && expression.expression == null)
            throw new IllegalStateException("Both argument and expression are null");
    }

    private ClassStructure getResultType(OperatorKeyword operator, ClassStructure argType) {
        return switch (operator) {
            case TILDE, PLUS, MINUS, DOUBLE_PLUS, DOUBLE_MINUS -> argType == INT ? INT : null;
            case EXCLAMATION_MARK -> argType == BOOLEAN ? BOOLEAN : null;
            default -> null;
        };
    }

}
