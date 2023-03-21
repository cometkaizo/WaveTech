package me.cometkaizo.wavetech.parser.syntaxes;

import me.cometkaizo.wavetech.parser.structures.VariableDeclaration;
import me.cometkaizo.wavetech.syntaxes.Syntax;
import me.cometkaizo.wavetech.syntaxes.SyntaxStructure;
import me.cometkaizo.wavetech.syntaxes.SyntaxSyntaxNodeBuilder;
import org.jetbrains.annotations.NotNull;

import static me.cometkaizo.wavetech.lexer.tokens.types.OperatorKeyword.SEMICOLON;
import static me.cometkaizo.wavetech.lexer.tokens.types.ModifierKeyword.FINAL;
import static me.cometkaizo.wavetech.lexer.tokens.types.ModifierKeyword.VOLATILE;

public class VariableDeclarationSyntax extends Syntax<VariableDeclaration> {

    private VariableDeclarationSyntax(boolean endWithSemicolon) {
        var builder = rootBuilder
                .optionally("modifier", FINAL, VOLATILE)
                .then("type", ClassAccessSyntax::getInstance)
                .thenIdentifier("name");

        if (endWithSemicolon)
            builder.split(
                    new SyntaxSyntaxNodeBuilder<>(AssignationExtensionSyntax::getInstance).withLabel("initializer"),
                    SEMICOLON.node()
            );
        else
            builder.optionally("initializer", AssignationExtensionSyntax::getNoSemicolonInstance);

    }

    public static VariableDeclarationSyntax getInstance() {
        return InstanceHolder.INSTANCE;
    }
    public static VariableDeclarationSyntax getNoSemicolonInstance() {
        return InstanceHolder.NO_SEMICOLON_INSTANCE;
    }

    private static class InstanceHolder {
        static final VariableDeclarationSyntax INSTANCE = new VariableDeclarationSyntax(true);
        static final VariableDeclarationSyntax NO_SEMICOLON_INSTANCE = new VariableDeclarationSyntax(false);
    }


    @Override
    public @NotNull VariableDeclaration getStructure(SyntaxStructure parent) {
        return new VariableDeclaration();
    }
}
