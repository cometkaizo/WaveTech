package me.cometkaizo.wavetech.parser.syntaxes;

import me.cometkaizo.wavetech.parser.structures.Identifier;
import me.cometkaizo.wavetech.syntaxes.Syntax;
import me.cometkaizo.wavetech.syntaxes.SyntaxStructure;
import org.jetbrains.annotations.NotNull;

import static me.cometkaizo.wavetech.lexer.tokens.types.TypeKeyword.*;

public class IdentifierSyntax extends Syntax<Identifier> {

    private IdentifierSyntax() {
        rootBuilder
                .then("name", SYMBOL)
        ;
    }

    @Override
    public @NotNull Identifier getStructure(SyntaxStructure parent) {
        return new Identifier();
    }

    public static IdentifierSyntax getInstance() {
        return InstanceHolder.INSTANCE;
    }

    private static class InstanceHolder {
        static final IdentifierSyntax INSTANCE = new IdentifierSyntax();

    }
}
