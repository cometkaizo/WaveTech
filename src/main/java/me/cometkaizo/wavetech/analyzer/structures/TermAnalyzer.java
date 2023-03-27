package me.cometkaizo.wavetech.analyzer.structures;

import me.cometkaizo.wavetech.analyzer.ResourcePool;
import me.cometkaizo.wavetech.analyzer.diagnostics.Diagnostic;
import me.cometkaizo.wavetech.analyzer.diagnostics.InapplicableOperatorDiagnostic;
import me.cometkaizo.wavetech.lexer.tokens.types.OperatorKeyword;
import me.cometkaizo.wavetech.parser.BuiltInClasses;
import me.cometkaizo.wavetech.parser.structures.ClassStructure;
import me.cometkaizo.wavetech.parser.structures.Factor;
import me.cometkaizo.wavetech.parser.structures.Term;

import java.util.List;

public class TermAnalyzer implements ProgramContextAnalyzer {
    boolean analyzed = false;
    private final Term term;

    public TermAnalyzer(Term term) {
        this.term = term;
    }

    @Override
    public void analyze(List<Diagnostic> problems, ResourcePool resources) {
        if (analyzed) return;

        Factor factor1 = term.factors.get(0);
        factor1.getAnalyzer().analyze(problems, resources);
        if (factor1.type == null) return;
        ClassStructure lastType = factor1.type;

        for (int index = 1; index < term.factors.size(); index++) {
            OperatorKeyword operator = term.operators.get(index - 1);
            Factor factor = term.factors.get(index);

            factor.getAnalyzer().analyze(problems, resources);
            if (factor.type == null) return;

            var newType = getResultType(operator, lastType, factor.type);

            if (newType == null) {
                problems.add(new InapplicableOperatorDiagnostic(operator, term, lastType, factor.type));
                return;
            }
            lastType = newType;
        }

        term.type = lastType;
        analyzed = true;
    }

    private ClassStructure getResultType(OperatorKeyword operator, ClassStructure leftType, ClassStructure rightType) {
        return switch (operator) {
            case ASTERISK, SLASH, PERCENT, DOUBLE_ASTERISK ->
                    leftType == BuiltInClasses.INT && rightType == BuiltInClasses.INT ? BuiltInClasses.INT : null;
            case LESS_THAN, LESS_THAN_OR_EQUAL, GREATER_THAN, GREATER_THAN_OR_EQUAL ->
                    leftType == BuiltInClasses.INT && rightType == BuiltInClasses.INT ? BuiltInClasses.BOOLEAN : null;
            case DOUBLE_EQUALS ->
                    leftType == BuiltInClasses.BOOLEAN && rightType == BuiltInClasses.BOOLEAN ? BuiltInClasses.BOOLEAN : null;
            default -> null;
        };
    }
}
