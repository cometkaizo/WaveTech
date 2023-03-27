package me.cometkaizo.wavetech.parser.syntaxes;

import me.cometkaizo.wavetech.parser.structures.EqualityExpression;
import me.cometkaizo.wavetech.syntaxes.Syntax;
import me.cometkaizo.wavetech.syntaxes.SyntaxStructure;
import org.jetbrains.annotations.Nullable;

import static me.cometkaizo.wavetech.lexer.tokens.types.OperatorKeyword.DOUBLE_EQUALS;
import static me.cometkaizo.wavetech.lexer.tokens.types.OperatorKeyword.EXCLAMATION_MARK_EQUALS;

public class EqualityExpressionSyntax extends Syntax<EqualityExpression> {

    private EqualityExpressionSyntax() {
        rootBuilder
                .oneOrMoreInterlace("expression",
                        RelationalExpressionSyntax::getInstance,
                        "operator",
                        DOUBLE_EQUALS.node(),
                        EXCLAMATION_MARK_EQUALS.node());
    }

    @Nullable
    @Override
    public EqualityExpression getStructure(@Nullable SyntaxStructure parent) {
        return new EqualityExpression();
    }

    public static EqualityExpressionSyntax getInstance() {
        return InstanceHolder.INSTANCE;
    }

    private static class InstanceHolder {
        static final EqualityExpressionSyntax INSTANCE = new EqualityExpressionSyntax();
    }

}
