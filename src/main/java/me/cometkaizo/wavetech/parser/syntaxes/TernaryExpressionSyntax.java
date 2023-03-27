package me.cometkaizo.wavetech.parser.syntaxes;

import me.cometkaizo.wavetech.parser.structures.TernaryExpression;
import me.cometkaizo.wavetech.syntaxes.Syntax;
import me.cometkaizo.wavetech.syntaxes.SyntaxStructure;
import org.jetbrains.annotations.Nullable;

import static me.cometkaizo.wavetech.lexer.tokens.types.OperatorKeyword.COLON;
import static me.cometkaizo.wavetech.lexer.tokens.types.OperatorKeyword.QUESTION_MARK;

public class TernaryExpressionSyntax extends Syntax<TernaryExpression> {

    private TernaryExpressionSyntax() {
        rootBuilder
                .then("nextExpression", LogicalOrExpressionSyntax::getInstance)
                .optionally(
                        QUESTION_MARK.node()
                                .then("leftArg", ExpressionSyntax::getInstance)
                                .then(COLON)
                                .then("rightArg", TernaryExpressionSyntax::getInstance)
                );
    }

    @Nullable
    @Override
    public TernaryExpression getStructure(@Nullable SyntaxStructure parent) {
        return new TernaryExpression();
    }

    public static TernaryExpressionSyntax getInstance() {
        return InstanceHolder.INSTANCE;
    }

    private static class InstanceHolder {
        static final TernaryExpressionSyntax INSTANCE = new TernaryExpressionSyntax();
    }

}
