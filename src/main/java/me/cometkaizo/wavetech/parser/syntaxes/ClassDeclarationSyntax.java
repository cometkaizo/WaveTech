package me.cometkaizo.wavetech.parser.syntaxes;

import me.cometkaizo.logging.LogUtils;
import me.cometkaizo.wavetech.parser.structures.ClassStructure;
import me.cometkaizo.wavetech.syntaxes.Syntax;
import me.cometkaizo.wavetech.syntaxes.SyntaxStructure;
import me.cometkaizo.wavetech.syntaxes.TokenTypeSyntaxNodeBuilder;
import org.jetbrains.annotations.NotNull;

import static me.cometkaizo.wavetech.lexer.tokens.types.DeclarationKeyword.CLASS;
import static me.cometkaizo.wavetech.lexer.tokens.types.OperatorKeyword.L_BRACE;
import static me.cometkaizo.wavetech.lexer.tokens.types.OperatorKeyword.R_BRACE;
import static me.cometkaizo.wavetech.lexer.tokens.types.ModifierKeyword.*;
import static me.cometkaizo.wavetech.lexer.tokens.types.VisibilityKeyword.PUBLIC;

public class ClassDeclarationSyntax extends Syntax<ClassStructure> {

    // (public)? ((sealed( abstract)?)|abstract|final)? class

    private ClassDeclarationSyntax() {

        LogUtils.debug();
        rootBuilder
                .optionally("visibility",
                        PUBLIC
                )
                .optionally(
                        new TokenTypeSyntaxNodeBuilder(SEALED).withLabel("modifier")
                                .optionally("modifier", ABSTRACT),
                        new TokenTypeSyntaxNodeBuilder(ABSTRACT).withLabel("modifier"),
                        new TokenTypeSyntaxNodeBuilder(FINAL).withLabel("modifier")
                )
                .then(CLASS)
                .thenIdentifier("name")
                .then(L_BRACE)
                .zeroOrMore("content",
                        MethodDeclarationSyntax::getInstance,
                        FieldDeclarationSyntax::getInstance)
                .then(R_BRACE);

    }

    public static ClassDeclarationSyntax getInstance() {
        return InstanceHolder.INSTANCE;
    }
    private static class InstanceHolder {
        static final ClassDeclarationSyntax INSTANCE = new ClassDeclarationSyntax();
    }

    @Override
    public @NotNull ClassStructure getStructure(SyntaxStructure parent) {
        return new ClassStructure();
    }
}
