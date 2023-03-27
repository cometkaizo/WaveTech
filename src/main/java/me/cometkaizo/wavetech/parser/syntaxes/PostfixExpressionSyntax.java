package me.cometkaizo.wavetech.parser.syntaxes;

import me.cometkaizo.util.CollectionUtils;
import me.cometkaizo.wavetech.lexer.tokens.types.OperatorKeyword;
import me.cometkaizo.wavetech.parser.structures.PostfixExpression;
import me.cometkaizo.wavetech.syntaxes.Syntax;
import me.cometkaizo.wavetech.syntaxes.SyntaxStructure;
import me.cometkaizo.wavetech.syntaxes.TokenTypeSyntaxNodeBuilder;
import org.jetbrains.annotations.Nullable;

import static me.cometkaizo.wavetech.lexer.tokens.types.OperatorKeyword.DOUBLE_MINUS;
import static me.cometkaizo.wavetech.lexer.tokens.types.OperatorKeyword.DOUBLE_PLUS;

public class PostfixExpressionSyntax extends Syntax<PostfixExpression> {

    private static final OperatorKeyword[] OPERATORS = {
            DOUBLE_PLUS,
            DOUBLE_MINUS
    };
    public static final TokenTypeSyntaxNodeBuilder[] OPERATOR_NODES = CollectionUtils.map(OPERATORS, TokenTypeSyntaxNodeBuilder::new, TokenTypeSyntaxNodeBuilder[]::new);

    private PostfixExpressionSyntax() {
        rootBuilder
                .then("expression", FactorSyntax::getInstance)
                .zeroOrMore("operator", OPERATOR_NODES);
    }

    @Nullable
    @Override
    public PostfixExpression getStructure(@Nullable SyntaxStructure parent) {
        return new PostfixExpression();
    }

    public static PostfixExpressionSyntax getInstance() {
        return InstanceHolder.INSTANCE;
    }

    private static class InstanceHolder {
        static final PostfixExpressionSyntax INSTANCE = new PostfixExpressionSyntax();
    }



}
