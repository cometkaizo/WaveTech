package me.cometkaizo.wavetech.parser.syntaxes;

import me.cometkaizo.util.CollectionUtils;
import me.cometkaizo.wavetech.lexer.tokens.TokenType;
import me.cometkaizo.wavetech.parser.structures.MultiplicativeExpression;
import me.cometkaizo.wavetech.syntaxes.Syntax;
import me.cometkaizo.wavetech.syntaxes.SyntaxStructure;
import me.cometkaizo.wavetech.syntaxes.TokenTypeSyntaxNodeBuilder;
import org.jetbrains.annotations.Nullable;

import static me.cometkaizo.wavetech.lexer.tokens.types.OperatorKeyword.*;

public class MultiplicativeExpressionSyntax extends Syntax<MultiplicativeExpression> {

    public static final TokenType[] OPERATORS = {
            ASTERISK,
            SLASH,
            PERCENT
    };
    public static final TokenTypeSyntaxNodeBuilder[] OPERATOR_NODES = CollectionUtils.map(OPERATORS, TokenTypeSyntaxNodeBuilder::new, TokenTypeSyntaxNodeBuilder[]::new);

    private MultiplicativeExpressionSyntax() {
        rootBuilder
                .oneOrMoreInterlace("expression", PrefixExpressionSyntax::getInstance, "operator", OPERATOR_NODES);
    }

    @Nullable
    @Override
    public MultiplicativeExpression getStructure(@Nullable SyntaxStructure parent) {
        return new MultiplicativeExpression();
    }

    public static MultiplicativeExpressionSyntax getInstance() {
        return InstanceHolder.INSTANCE;
    }

    private static class InstanceHolder {
        static final MultiplicativeExpressionSyntax INSTANCE = new MultiplicativeExpressionSyntax();
    }

}
