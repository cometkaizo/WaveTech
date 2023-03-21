package me.cometkaizo.wavetech.parser.syntaxes;

import me.cometkaizo.wavetech.parser.structures.MethodAccessor;
import me.cometkaizo.wavetech.syntaxes.Syntax;
import me.cometkaizo.wavetech.syntaxes.SyntaxStructure;
import me.cometkaizo.wavetech.syntaxes.SyntaxSyntaxNodeBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static me.cometkaizo.wavetech.lexer.tokens.types.OperatorKeyword.*;

public class MethodAccessSyntax extends Syntax<MethodAccessor> {

    private MethodAccessSyntax(boolean endWithSemicolon) {
        var builder = rootBuilder
                .thenIdentifier("name")
                .then(L_PAREN)
                .then(ExpressionListSyntax::getInstance)
                .then(R_PAREN);

        if (endWithSemicolon) builder.then(SEMICOLON);
    }

    @Override
    public @Nullable MethodAccessor getStructure(@Nullable SyntaxStructure parent) {
        return new MethodAccessor(parent);
    }

    private static class ExpressionListSyntax extends Syntax<MethodAccessor> {

        private ExpressionListSyntax() {
            rootBuilder
                    .optionally(new SyntaxSyntaxNodeBuilder<>(ExpressionSyntax::getInstance).withLabel("parameter")
                            .zeroOrMore(ExtraExpressionSyntax::getInstance));
        }

        @Override
        public @Nullable MethodAccessor getStructure(@Nullable SyntaxStructure parent) {
            return (MethodAccessor) parent;
        }

        public static @NotNull ExpressionListSyntax getInstance() {
            return InstanceHolder.INSTANCE;
        }

        private static class InstanceHolder {
            static final ExpressionListSyntax INSTANCE = new ExpressionListSyntax();
        }
    }


    private static class ExtraExpressionSyntax extends Syntax<MethodAccessor> {

        private ExtraExpressionSyntax() {
            rootBuilder
                    .then(COMMA)
                    .then("parameter", ExpressionSyntax::getInstance);
        }

        @Override
        public @Nullable MethodAccessor getStructure(@Nullable SyntaxStructure parent) {
            return (MethodAccessor) parent;
        }

        public static ExtraExpressionSyntax getInstance() {
            return InstanceHolder.INSTANCE;
        }

        private static class InstanceHolder {
            static final ExtraExpressionSyntax INSTANCE = new ExtraExpressionSyntax();
        }
    }

    public static MethodAccessSyntax getInstance() {
        return InstanceHolder.INSTANCE;
    }

    public static MethodAccessSyntax getNoSemicolonInstance() {
        return InstanceHolder.NO_SEMICOLON_INSTANCE;
    }

    private static class InstanceHolder {
        static final MethodAccessSyntax INSTANCE = new MethodAccessSyntax(true);
        static final MethodAccessSyntax NO_SEMICOLON_INSTANCE = new MethodAccessSyntax(false);
    }
}
