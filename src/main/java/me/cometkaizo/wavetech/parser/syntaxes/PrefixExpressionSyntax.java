package me.cometkaizo.wavetech.parser.syntaxes;

import me.cometkaizo.util.CollectionUtils;
import me.cometkaizo.wavetech.lexer.tokens.TokenType;
import me.cometkaizo.wavetech.parser.structures.PrefixExpression;
import me.cometkaizo.wavetech.syntaxes.*;
import org.jetbrains.annotations.Nullable;

import static me.cometkaizo.wavetech.lexer.tokens.types.OperatorKeyword.*;

public class PrefixExpressionSyntax extends Syntax<PrefixExpression> {

    public static final TokenType[] OPERATORS = {
            PLUS,
            MINUS,
            DOUBLE_PLUS,
            DOUBLE_MINUS,
            TILDE,
            EXCLAMATION_MARK
    };
    public static final TokenTypeSyntaxNodeBuilder[] OPERATOR_NODES = CollectionUtils.map(OPERATORS, TokenTypeSyntaxNodeBuilder::new, TokenTypeSyntaxNodeBuilder[]::new);

    private PrefixExpressionSyntax() {
        rootBuilder
                .split(
                        new SyntaxSyntaxNodeBuilder<>(PostfixExpressionSyntax::getInstance).withLabel("expression"),
                        new EmptySyntaxNodeBuilder()
                                .split("operator", OPERATORS)
                                .then("argument", PrefixExpressionSyntax::getInstance)
                );
                // TODO: 2023-03-20 Add 'cast' syntax
    }

    @Nullable
    @Override
    public PrefixExpression getStructure(@Nullable SyntaxStructure parent) {
        return new PrefixExpression();
    }

    public static PrefixExpressionSyntax getInstance() {
        return InstanceHolder.INSTANCE;
    }

    private static class InstanceHolder {
        static final PrefixExpressionSyntax INSTANCE = new PrefixExpressionSyntax();
    }

}
