package me.cometkaizo.wavetech.analyzer.diagnostics;

import me.cometkaizo.wavetech.lexer.tokens.types.OperatorKeyword;
import me.cometkaizo.wavetech.parser.structures.ClassStructure;
import me.cometkaizo.wavetech.syntaxes.SyntaxStructure;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class InapplicableOperatorDiagnostic extends ErrorDiagnostic {

    public InapplicableOperatorDiagnostic(OperatorKeyword operator, SyntaxStructure offendingStructure, ClassStructure... types) {
        this(List.of(operator), offendingStructure, types);
    }
    public InapplicableOperatorDiagnostic(List<OperatorKeyword> operators, SyntaxStructure offendingStructure, ClassStructure... types) {
        super(createMessage(operators, types), offendingStructure.getStartPosition(), offendingStructure.getEndPosition());
    }

    private static String createMessage(List<OperatorKeyword> operators, ClassStructure[] types) {
        String s = operators.size() > 1 ? "s" : "";
        String formattedOperators = operators.stream().map(OperatorKeyword::symbol).collect(Collectors.joining(" ", "'", "'"));
        String formattedTypes = Arrays.stream(types).map(c -> c.name.name).collect(Collectors.joining("', '", "'", "'"));
        return "Operator" + s + ' ' + formattedOperators + " cannot be applied to " + formattedTypes;
    }
}
