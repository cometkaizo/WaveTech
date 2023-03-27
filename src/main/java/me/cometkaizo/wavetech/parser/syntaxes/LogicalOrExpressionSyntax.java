package me.cometkaizo.wavetech.parser.syntaxes;

import me.cometkaizo.wavetech.parser.structures.LogicalOrExpression;
import me.cometkaizo.wavetech.syntaxes.Syntax;
import me.cometkaizo.wavetech.syntaxes.SyntaxStructure;
import org.jetbrains.annotations.Nullable;

import static me.cometkaizo.wavetech.lexer.tokens.types.OperatorKeyword.DOUBLE_PIPE;

public class LogicalOrExpressionSyntax extends Syntax<LogicalOrExpression> {

    private LogicalOrExpressionSyntax() {
        rootBuilder
                .oneOrMoreInterlace("expression", LogicalAndExpressionSyntax::getInstance, "operator", DOUBLE_PIPE.node());
    }

    @Nullable
    @Override
    public LogicalOrExpression getStructure(@Nullable SyntaxStructure parent) {
        return new LogicalOrExpression();
    }

    public static LogicalOrExpressionSyntax getInstance() {
        return InstanceHolder.INSTANCE;
    }

    private static class InstanceHolder {
        static final LogicalOrExpressionSyntax INSTANCE = new LogicalOrExpressionSyntax();
    }

}
