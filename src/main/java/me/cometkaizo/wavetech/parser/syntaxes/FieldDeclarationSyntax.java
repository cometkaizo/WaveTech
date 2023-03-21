package me.cometkaizo.wavetech.parser.syntaxes;

import me.cometkaizo.wavetech.parser.structures.ClassStructure;
import me.cometkaizo.wavetech.parser.structures.FieldDeclaration;
import me.cometkaizo.wavetech.syntaxes.Syntax;
import me.cometkaizo.wavetech.syntaxes.SyntaxStructure;
import me.cometkaizo.wavetech.syntaxes.SyntaxSyntaxNodeBuilder;
import org.jetbrains.annotations.NotNull;

import static me.cometkaizo.wavetech.lexer.tokens.types.OperatorKeyword.SEMICOLON;
import static me.cometkaizo.wavetech.lexer.tokens.types.ModifierKeyword.*;
import static me.cometkaizo.wavetech.lexer.tokens.types.VisibilityKeyword.*;

public class FieldDeclarationSyntax extends Syntax<FieldDeclaration> {


    public FieldDeclarationSyntax() {

        rootBuilder
                .optionally("visibility", PUBLIC, PROTECTED, PACKAGE_PRIVATE, PRIVATE)
                .optionally("modifier", STATIC, TRANSIENT)
                .optionally("modifier", FINAL, VOLATILE)
                .then("type", ClassAccessSyntax::getInstance)
                .thenIdentifier("name")
                .split(
                        new SyntaxSyntaxNodeBuilder<>(AssignationExtensionSyntax::getInstance).withLabel("initializer"),
                        SEMICOLON.node()
                );
    }


    @Override
    public @NotNull FieldDeclaration getStructure(SyntaxStructure parent) {
        return new FieldDeclaration((ClassStructure) parent);
    }

    public static FieldDeclarationSyntax getInstance() {
        return InstanceHolder.INSTANCE;
    }

    private static class InstanceHolder {
        static final FieldDeclarationSyntax INSTANCE = new FieldDeclarationSyntax();
    }

}
