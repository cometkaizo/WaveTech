package me.cometkaizo.wavetech.parser.syntaxes;

import me.cometkaizo.wavetech.lexer.tokens.types.Literal;
import me.cometkaizo.wavetech.parser.structures.LiteralStructure;
import me.cometkaizo.wavetech.syntaxes.Syntax;
import me.cometkaizo.wavetech.syntaxes.SyntaxStructure;
import org.jetbrains.annotations.NotNull;

public class LiteralSyntax extends Syntax<LiteralStructure> {

    private LiteralSyntax() {
        rootBuilder.split("token", Literal.values());
    }

    @Override
    public @NotNull LiteralStructure getStructure(SyntaxStructure parent) {
        return new LiteralStructure();
    }

    public static LiteralSyntax getInstance() {
        return InstanceHolder.INSTANCE;
    }

    private static class InstanceHolder {
        static final LiteralSyntax INSTANCE = new LiteralSyntax();
    }
}
