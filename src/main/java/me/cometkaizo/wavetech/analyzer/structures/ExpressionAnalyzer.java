package me.cometkaizo.wavetech.analyzer.structures;

import me.cometkaizo.wavetech.analyzer.ResourcePool;
import me.cometkaizo.wavetech.analyzer.diagnostics.Diagnostic;
import me.cometkaizo.wavetech.lexer.tokens.types.OperatorKeyword;
import me.cometkaizo.wavetech.parser.BuiltInClasses;
import me.cometkaizo.wavetech.parser.structures.ClassStructure;
import me.cometkaizo.wavetech.parser.structures.Expression;
import me.cometkaizo.wavetech.parser.structures.VariableDeclaration;

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
        analyzed = true;
        throwIfIllegalState();

        if (expression.varAssignment != null) {
            expression.varAssignment.getAnalyzer().analyze(problems, resources);
            var assignedVariableAccessor = expression.varAssignment.variableAccessor;
            var assignedVariable = (VariableDeclaration) resources.getResourceOrAnalyze(assignedVariableAccessor, problems);
            if (assignedVariable != null) expression.type = assignedVariable.getTypeOrAnalyze(resources, problems);
        } else {
            expression.ternary.getAnalyzer().analyze(problems, resources);
            expression.type = expression.ternary.type;
        }
    }

    private void throwIfIllegalState() {
        if (expression.varAssignment == null && expression.ternary == null)
            throw new IllegalStateException("Both variable assignment and ternary are null");
    }

    private ClassStructure getResultType(OperatorKeyword operator, ClassStructure leftType, ClassStructure rightType) {
        return switch (operator) {
            case PLUS, MINUS -> leftType == BuiltInClasses.INT && rightType == BuiltInClasses.INT ? BuiltInClasses.INT : null;
            case DOUBLE_AMPERSAND, DOUBLE_PIPE -> leftType == BuiltInClasses.BOOLEAN && rightType == BuiltInClasses.BOOLEAN ? BuiltInClasses.BOOLEAN : null;
            default -> null;
        };
    }
}
