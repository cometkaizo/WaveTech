package me.cometkaizo.wavetech.parser.syntaxes;

import me.cometkaizo.wavetech.parser.structures.BitwiseXorExpression;
import me.cometkaizo.wavetech.syntaxes.Syntax;
import me.cometkaizo.wavetech.syntaxes.SyntaxStructure;
import org.jetbrains.annotations.Nullable;

import static me.cometkaizo.wavetech.lexer.tokens.types.OperatorKeyword.CARET;

public class BitwiseXorExpressionSyntax extends Syntax<BitwiseXorExpression> {

    private BitwiseXorExpressionSyntax() {
        rootBuilder
                .oneOrMoreInterlace("expression", BitwiseAndExpressionSyntax::getInstance, "operator", CARET.node());
    }

    @Nullable
    @Override
    public BitwiseXorExpression getStructure(@Nullable SyntaxStructure parent) {
        return new BitwiseXorExpression();
    }

    public static BitwiseXorExpressionSyntax getInstance() {
        return InstanceHolder.INSTANCE;
    }

    private static class InstanceHolder {
        static final BitwiseXorExpressionSyntax INSTANCE = new BitwiseXorExpressionSyntax();
    }

}
