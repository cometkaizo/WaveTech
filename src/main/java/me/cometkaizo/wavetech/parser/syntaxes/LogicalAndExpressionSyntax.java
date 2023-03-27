package me.cometkaizo.wavetech.parser.syntaxes;

import me.cometkaizo.wavetech.parser.structures.LogicalAndExpression;
import me.cometkaizo.wavetech.syntaxes.Syntax;
import me.cometkaizo.wavetech.syntaxes.SyntaxStructure;
import org.jetbrains.annotations.Nullable;

import static me.cometkaizo.wavetech.lexer.tokens.types.OperatorKeyword.DOUBLE_AMPERSAND;

public class LogicalAndExpressionSyntax extends Syntax<LogicalAndExpression> {

    private LogicalAndExpressionSyntax() {
        rootBuilder
                .oneOrMoreInterlace("expression", BitwiseOrExpressionSyntax::getInstance, "operator", DOUBLE_AMPERSAND.node());
    }

    @Nullable
    @Override
    public LogicalAndExpression getStructure(@Nullable SyntaxStructure parent) {
        return new LogicalAndExpression();
    }

    public static LogicalAndExpressionSyntax getInstance() {
        return InstanceHolder.INSTANCE;
    }

    private static class InstanceHolder {
        static final LogicalAndExpressionSyntax INSTANCE = new LogicalAndExpressionSyntax();
    }

}
