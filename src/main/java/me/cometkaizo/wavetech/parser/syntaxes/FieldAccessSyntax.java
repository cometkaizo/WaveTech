package me.cometkaizo.wavetech.parser.syntaxes;

import me.cometkaizo.wavetech.parser.structures.FieldAccessor;
import me.cometkaizo.wavetech.syntaxes.Syntax;
import me.cometkaizo.wavetech.syntaxes.SyntaxStructure;
import org.jetbrains.annotations.Nullable;

import static me.cometkaizo.wavetech.lexer.tokens.types.ReferenceKeyword.SUPER;
import static me.cometkaizo.wavetech.lexer.tokens.types.ReferenceKeyword.THIS;

public class FieldAccessSyntax extends Syntax<FieldAccessor> {

    private FieldAccessSyntax() {
        rootBuilder
                .split(THIS, SUPER)
                .thenIdentifier("variableName");
    }

    @Override
    public @Nullable FieldAccessor getStructure(@Nullable SyntaxStructure parent) {
        return new FieldAccessor();
    }

    public static FieldAccessSyntax getInstance() {
        return InstanceHolder.INSTANCE;
    }

    private static class InstanceHolder {
        static final FieldAccessSyntax INSTANCE = new FieldAccessSyntax();
    }
}
