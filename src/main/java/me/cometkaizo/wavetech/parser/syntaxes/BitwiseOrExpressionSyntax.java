package me.cometkaizo.wavetech.parser.syntaxes;

import me.cometkaizo.wavetech.parser.structures.BitwiseOrExpression;
import me.cometkaizo.wavetech.syntaxes.Syntax;
import me.cometkaizo.wavetech.syntaxes.SyntaxStructure;
import org.jetbrains.annotations.Nullable;

import static me.cometkaizo.wavetech.lexer.tokens.types.OperatorKeyword.PIPE;

public class BitwiseOrExpressionSyntax extends Syntax<BitwiseOrExpression> {

    private BitwiseOrExpressionSyntax() {
        rootBuilder
                .oneOrMoreInterlace("expression", BitwiseXorExpressionSyntax::getInstance, "operator", PIPE.node());
    }

    @Nullable
    @Override
    public BitwiseOrExpression getStructure(@Nullable SyntaxStructure parent) {
        return new BitwiseOrExpression();
    }

    public static BitwiseOrExpressionSyntax getInstance() {
        return InstanceHolder.INSTANCE;
    }

    private static class InstanceHolder {
        static final BitwiseOrExpressionSyntax INSTANCE = new BitwiseOrExpressionSyntax();
    }

}
