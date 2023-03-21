package me.cometkaizo.wavetech.parser.syntaxes;

import me.cometkaizo.wavetech.parser.structures.VariableAccessor;
import me.cometkaizo.wavetech.syntaxes.Syntax;
import me.cometkaizo.wavetech.syntaxes.SyntaxStructure;
import org.jetbrains.annotations.Nullable;

public class VariableAccessSyntax extends Syntax<VariableAccessor> {

    private VariableAccessSyntax() {
        rootBuilder.thenIdentifier("variableName");
    }

    @Override
    public @Nullable VariableAccessor getStructure(@Nullable SyntaxStructure parent) {
        return new VariableAccessor();
    }

    public static VariableAccessSyntax getInstance() {
        return InstanceHolder.INSTANCE;
    }

    private static class InstanceHolder {
        static final VariableAccessSyntax INSTANCE = new VariableAccessSyntax();
    }
}
