package me.cometkaizo.wavetech.parser.syntaxes;

import me.cometkaizo.util.CollectionUtils;
import me.cometkaizo.wavetech.lexer.tokens.TokenType;
import me.cometkaizo.wavetech.parser.structures.ShiftExpression;
import me.cometkaizo.wavetech.syntaxes.Syntax;
import me.cometkaizo.wavetech.syntaxes.SyntaxStructure;
import me.cometkaizo.wavetech.syntaxes.TokenTypeSyntaxNodeBuilder;
import org.jetbrains.annotations.Nullable;

import static me.cometkaizo.wavetech.lexer.tokens.types.OperatorKeyword.*;

public class ShiftExpressionSyntax extends Syntax<ShiftExpression> {

    public static final TokenType[] OPERATORS = {
            L_SHIFT,
            R_SHIFT,
            TRIPLE_R_SHIFT
    };
    public static final TokenTypeSyntaxNodeBuilder[] OPERATOR_NODES = CollectionUtils.map(OPERATORS, TokenTypeSyntaxNodeBuilder::new, TokenTypeSyntaxNodeBuilder[]::new);

    private ShiftExpressionSyntax() {
        rootBuilder
                .oneOrMoreInterlace("expression", AdditiveExpressionSyntax::getInstance, "operator", OPERATOR_NODES);
    }

    @Nullable
    @Override
    public ShiftExpression getStructure(@Nullable SyntaxStructure parent) {
        return new ShiftExpression();
    }

    public static ShiftExpressionSyntax getInstance() {
        return InstanceHolder.INSTANCE;
    }

    private static class InstanceHolder {
        static final ShiftExpressionSyntax INSTANCE = new ShiftExpressionSyntax();
    }

}
