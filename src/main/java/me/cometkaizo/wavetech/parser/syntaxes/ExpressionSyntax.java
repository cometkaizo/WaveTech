package me.cometkaizo.wavetech.parser.syntaxes;

import me.cometkaizo.wavetech.parser.structures.Expression;
import me.cometkaizo.wavetech.syntaxes.Syntax;
import me.cometkaizo.wavetech.syntaxes.SyntaxStructure;
import org.jetbrains.annotations.NotNull;

import static me.cometkaizo.wavetech.lexer.tokens.types.OperatorKeyword.*;

public class ExpressionSyntax extends Syntax<Expression> {

    private ExpressionSyntax() {
        rootBuilder
                .then("term", TermSyntax::getInstance)
                .zeroOrMore(ExtraTermSyntax::getInstance);
    }

    public static class ExtraTermSyntax extends Syntax<Expression> {

        private ExtraTermSyntax() {
            rootBuilder
                    .split("operator",
                            PLUS,
                            MINUS,
                            DOUBLE_PIPE,
                            DOUBLE_AMPERSAND
                    )
                    .then("term", TermSyntax::getInstance);
        }

        @Override
        public @NotNull Expression getStructure(SyntaxStructure parent) {
            return (Expression) parent;
        }

        public static ExtraTermSyntax getInstance() {
            return InstanceHolder.INSTANCE;
        }

        private static class InstanceHolder {
            static final ExtraTermSyntax INSTANCE = new ExtraTermSyntax();
        }

    }


    public static ExpressionSyntax getInstance() {
        return InstanceHolder.INSTANCE;
    }

    private static class InstanceHolder {
        static final ExpressionSyntax INSTANCE = new ExpressionSyntax();
    }

    @Override
    public @NotNull Expression getStructure(SyntaxStructure parent) {
        return new Expression();
    }
}
