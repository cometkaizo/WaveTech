package me.cometkaizo.wavetech.analyzer.structures;

import me.cometkaizo.wavetech.analyzer.ResourcePool;
import me.cometkaizo.wavetech.analyzer.diagnostics.Diagnostic;
import me.cometkaizo.wavetech.analyzer.diagnostics.InapplicableOperatorDiagnostic;
import me.cometkaizo.wavetech.analyzer.diagnostics.IncompatibleTypesDiagnostic;
import me.cometkaizo.wavetech.parser.structures.ClassStructure;
import me.cometkaizo.wavetech.parser.structures.TernaryExpression;

import java.util.List;

import static me.cometkaizo.wavetech.lexer.tokens.types.OperatorKeyword.COLON;
import static me.cometkaizo.wavetech.lexer.tokens.types.OperatorKeyword.QUESTION_MARK;
import static me.cometkaizo.wavetech.parser.BuiltInClasses.BOOLEAN;
import static me.cometkaizo.wavetech.parser.BuiltInClasses.OBJECT;

public class TernaryExpressionAnalyzer implements ProgramContextAnalyzer {
    private final TernaryExpression expression;
    boolean analyzed = false;

    public TernaryExpressionAnalyzer(TernaryExpression expression) {
        this.expression = expression;
    }

    @Override
    public void analyze(List<Diagnostic> problems, ResourcePool resources) {
        if (analyzed) return;
        analyzed = true;
        throwIfIllegalState();

        expression.nextExpression.getAnalyzer().analyze(problems, resources);
        var conditionType = expression.nextExpression.type;

        if (expression.trueOption == null) {
            expression.type = conditionType;
        } else if (conditionType != BOOLEAN) {
            addIncompatibleTypesDiagnostic(problems);
        } else {
            expression.trueOption.getAnalyzer().analyze(problems, resources);
            expression.falseOption.getAnalyzer().analyze(problems, resources);
            var leftType = expression.trueOption.type;
            var rightType = expression.falseOption.type;

            var resultType = getResultType(leftType, rightType);
            if (resultType == null) addInapplicableOperatorDiagnostic(problems);

            expression.type = resultType;
        }
    }

    private void addIncompatibleTypesDiagnostic(List<Diagnostic> problems) {
        problems.add(new IncompatibleTypesDiagnostic(expression.nextExpression.type, BOOLEAN, expression.nextExpression));
    }

    private void addInapplicableOperatorDiagnostic(List<Diagnostic> problems) {
        problems.add(new InapplicableOperatorDiagnostic(List.of(QUESTION_MARK, COLON),
                expression,
                expression.trueOption.type,
                expression.falseOption.type));
    }

    private void throwIfIllegalState() {
        if (expression.nextExpression == null || expression.trueOption == null ^ expression.falseOption == null)
            throw new IllegalStateException("Either next expression or arguments are null");
    }

    private static ClassStructure getResultType(ClassStructure leftType, ClassStructure rightType) {
        return OBJECT;
    }

}
