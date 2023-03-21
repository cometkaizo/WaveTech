package me.cometkaizo.wavetech.parser.syntaxes;

import me.cometkaizo.wavetech.parser.structures.ClassStructure;
import me.cometkaizo.wavetech.parser.structures.MethodStructure;
import me.cometkaizo.wavetech.syntaxes.Syntax;
import me.cometkaizo.wavetech.syntaxes.SyntaxStructure;
import me.cometkaizo.wavetech.syntaxes.SyntaxSyntaxNodeBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static me.cometkaizo.wavetech.lexer.tokens.types.ModifierKeyword.*;
import static me.cometkaizo.wavetech.lexer.tokens.types.OperatorKeyword.*;
import static me.cometkaizo.wavetech.lexer.tokens.types.PrimitiveType.VOID;
import static me.cometkaizo.wavetech.lexer.tokens.types.VisibilityKeyword.*;

public class MethodDeclarationSyntax extends Syntax<MethodStructure> {


    // (public|protected|private)?
    // (static)?
    // (abstract|final)?
    // (_primitive-type_|_object-type_|void)
    // _symbol_
    // \(
    // ((_p_|_o_) _symbol_(, (_p_|_o_) _symbol_)*)?
    // \)

    public MethodDeclarationSyntax() {

        rootBuilder
                .optionally("visibility",
                        PUBLIC,
                        PROTECTED,
                        PRIVATE
                )
                .optionally("modifier",
                        STATIC
                )
                .optionally("modifier",
                        ABSTRACT,
                        FINAL
                )
                .then(ReturnTypeSyntax::getInstance)
                .thenIdentifier("name")
                .then(L_PAREN)
                .then(ParameterListSyntax::getInstance)
                .then(R_PAREN)
                .then("body", BlockSyntax::getInstance);

    }

    @Override
    public @NotNull MethodStructure getStructure(SyntaxStructure parent) {
        return new MethodStructure((ClassStructure) parent);
    }

    private static class ParameterListSyntax extends Syntax<MethodStructure> {

        private ParameterListSyntax() {
            rootBuilder
                    .optionally(new SyntaxSyntaxNodeBuilder<>(ParameterSyntax::getInstance).withLabel("parameter")
                            .zeroOrMore(ExtraParameterSyntax::getInstance));
        }

        @Override
        public @Nullable MethodStructure getStructure(@Nullable SyntaxStructure parent) {
            return (MethodStructure) parent;
        }

        public static @NotNull ParameterListSyntax getInstance() {
            return InstanceHolder.INSTANCE;
        }

        private static class InstanceHolder {
            static final ParameterListSyntax INSTANCE = new ParameterListSyntax();
        }
    }

    private static class ReturnTypeSyntax extends Syntax<MethodStructure> {

        private ReturnTypeSyntax() {
            rootBuilder.split(
                    VOID.node(),
                    new SyntaxSyntaxNodeBuilder<>(ClassAccessSyntax::getInstance).withLabel("returnType")
            );
        }

        @Override
        public @Nullable MethodStructure getStructure(@Nullable SyntaxStructure parent) {
            return (MethodStructure) parent;
        }

        public static @NotNull ReturnTypeSyntax getInstance() {
            return InstanceHolder.INSTANCE;
        }

        private static class InstanceHolder {
            static final ReturnTypeSyntax INSTANCE = new ReturnTypeSyntax();
        }
    }

    private static class ExtraParameterSyntax extends Syntax<MethodStructure> {

        private ExtraParameterSyntax() {

            rootBuilder
                    .then(COMMA)
                    .then("parameter", ParameterSyntax::getInstance);

        }

        @Override
        public @NotNull MethodStructure getStructure(SyntaxStructure parent) {
            return (MethodStructure) parent;
        }

        public static @NotNull ExtraParameterSyntax getInstance() {
            return InstanceHolder.INSTANCE;
        }

        private static class InstanceHolder {
            static final ExtraParameterSyntax INSTANCE = new ExtraParameterSyntax();
        }
    }

    public static class ParameterSyntax extends Syntax<MethodStructure.Parameter> {

        private ParameterSyntax() {
            rootBuilder
                    .then("type", ClassAccessSyntax::getInstance)
                    .thenIdentifier("name");
        }

        @Override
        public @NotNull MethodStructure.Parameter getStructure(SyntaxStructure parent) {
            return new MethodStructure.Parameter();
        }

        public static @NotNull ParameterSyntax getInstance() {
            return InstanceHolder.INSTANCE;
        }

        private static class InstanceHolder {
            static final ParameterSyntax INSTANCE = new ParameterSyntax();
        }
    }


    public static MethodDeclarationSyntax getInstance() {
        return InstanceHolder.INSTANCE;
    }

    private static class InstanceHolder {
        static final MethodDeclarationSyntax INSTANCE = new MethodDeclarationSyntax();
    }

}
