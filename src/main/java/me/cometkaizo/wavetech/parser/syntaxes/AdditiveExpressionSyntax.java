package me.cometkaizo.wavetech.parser.syntaxes;

import me.cometkaizo.util.CollectionUtils;
import me.cometkaizo.wavetech.lexer.tokens.TokenType;
import me.cometkaizo.wavetech.parser.structures.AdditiveExpression;
import me.cometkaizo.wavetech.syntaxes.Syntax;
import me.cometkaizo.wavetech.syntaxes.SyntaxStructure;
import me.cometkaizo.wavetech.syntaxes.TokenTypeSyntaxNodeBuilder;
import org.jetbrains.annotations.Nullable;

import static me.cometkaizo.wavetech.lexer.tokens.types.OperatorKeyword.MINUS;
import static me.cometkaizo.wavetech.lexer.tokens.types.OperatorKeyword.PLUS;

public class AdditiveExpressionSyntax extends Syntax<AdditiveExpression> {

    public static final TokenType[] OPERATORS = {
            PLUS,
            MINUS
    };
    public static final TokenTypeSyntaxNodeBuilder[] OPERATOR_NODES = CollectionUtils.map(OPERATORS, TokenTypeSyntaxNodeBuilder::new, TokenTypeSyntaxNodeBuilder[]::new);

    private AdditiveExpressionSyntax() {
        rootBuilder
                .oneOrMoreInterlace("expression", MultiplicativeExpressionSyntax::getInstance, "operator", OPERATOR_NODES);
    }

    @Nullable
    @Override
    public AdditiveExpression getStructure(@Nullable SyntaxStructure parent) {
        return new AdditiveExpression();
    }

    public static AdditiveExpressionSyntax getInstance() {
        return InstanceHolder.INSTANCE;
    }

    private static class InstanceHolder {
        static final AdditiveExpressionSyntax INSTANCE = new AdditiveExpressionSyntax();
    }

}
