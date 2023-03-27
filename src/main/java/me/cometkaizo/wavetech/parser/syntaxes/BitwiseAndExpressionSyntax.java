package me.cometkaizo.wavetech.parser.syntaxes;

import me.cometkaizo.wavetech.parser.structures.BitwiseAndExpression;
import me.cometkaizo.wavetech.syntaxes.Syntax;
import me.cometkaizo.wavetech.syntaxes.SyntaxStructure;
import org.jetbrains.annotations.Nullable;

import static me.cometkaizo.wavetech.lexer.tokens.types.OperatorKeyword.AMPERSAND;

public class BitwiseAndExpressionSyntax extends Syntax<BitwiseAndExpression> {

    private BitwiseAndExpressionSyntax() {
        rootBuilder
                .oneOrMoreInterlace("expression", EqualityExpressionSyntax::getInstance, "operator", AMPERSAND.node());
    }

    @Nullable
    @Override
    public BitwiseAndExpression getStructure(@Nullable SyntaxStructure parent) {
        return new BitwiseAndExpression();
    }

    public static BitwiseAndExpressionSyntax getInstance() {
        return InstanceHolder.INSTANCE;
    }

    private static class InstanceHolder {
        static final BitwiseAndExpressionSyntax INSTANCE = new BitwiseAndExpressionSyntax();
    }

}
