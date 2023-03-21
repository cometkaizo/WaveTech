package me.cometkaizo.wavetech.parser.syntaxes;

import me.cometkaizo.wavetech.parser.structures.ClassAccessor;
import me.cometkaizo.wavetech.syntaxes.Syntax;
import me.cometkaizo.wavetech.syntaxes.SyntaxStructure;
import org.jetbrains.annotations.Nullable;

public class ClassAccessSyntax extends Syntax<ClassAccessor> {

    private ClassAccessSyntax() {
        rootBuilder.thenIdentifier("className");
    }

    @Override
    public @Nullable ClassAccessor getStructure(@Nullable SyntaxStructure parent) {
        return new ClassAccessor();
    }

    public static ClassAccessSyntax getInstance() {
        return InstanceHolder.INSTANCE;
    }

    private static class InstanceHolder {
        static final ClassAccessSyntax INSTANCE = new ClassAccessSyntax();
    }
}
