package me.cometkaizo.wavetech.parser.syntaxes;

import me.cometkaizo.util.CollectionUtils;
import me.cometkaizo.wavetech.lexer.tokens.TokenType;
import me.cometkaizo.wavetech.parser.structures.RelationalExpression;
import me.cometkaizo.wavetech.syntaxes.Syntax;
import me.cometkaizo.wavetech.syntaxes.SyntaxStructure;
import me.cometkaizo.wavetech.syntaxes.TokenTypeSyntaxNodeBuilder;
import org.jetbrains.annotations.Nullable;

import static me.cometkaizo.wavetech.lexer.tokens.types.OperatorKeyword.*;

public class RelationalExpressionSyntax extends Syntax<RelationalExpression> {

    public static final TokenType[] OPERATORS = {
            LESS_THAN,
            LESS_THAN_OR_EQUAL,
            GREATER_THAN,
            GREATER_THAN_OR_EQUAL
    };
    public static final TokenTypeSyntaxNodeBuilder[] OPERATOR_NODES = CollectionUtils.map(OPERATORS, TokenTypeSyntaxNodeBuilder::new, TokenTypeSyntaxNodeBuilder[]::new);

    private RelationalExpressionSyntax() {
        rootBuilder
                .oneOrMoreInterlace("expression", ShiftExpressionSyntax::getInstance, "operator", OPERATOR_NODES);
                // TODO: 2023-03-20 Add 'instanceof' syntax
    }

    @Nullable
    @Override
    public RelationalExpression getStructure(@Nullable SyntaxStructure parent) {
        return new RelationalExpression();
    }

    public static RelationalExpressionSyntax getInstance() {
        return InstanceHolder.INSTANCE;
    }

    private static class InstanceHolder {
        static final RelationalExpressionSyntax INSTANCE = new RelationalExpressionSyntax();
    }

}
