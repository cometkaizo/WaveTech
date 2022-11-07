package me.cometkaizo.temp.nodes;

import me.cometkaizo.wavetech.lexer.CompilationException;
import me.cometkaizo.wavetech.lexer.tokens.types.VisibilityModifier;

import java.util.HashSet;

public class SourceFileNode extends DeclaredNode {


    public SourceFileNode(String name) {
        super(null, null, new HashSet<>(0), name, new HashSet<>(0));
    }

    @Override
    protected VisibilityModifier getEffectiveVisibility() {
        return visibilityModifier;
    }

    @Override
    protected void throwIfInvalidModifiers() {
        if (!propertyModifiers.isEmpty()) throw new CompilationException("SourceFileToken cannot have property modifiers");
    }

    @Override
    protected void throwIfInvalidPropertyDeclaration() {
        if (!propertyDeclarations.isEmpty()) throw new CompilationException("SourceFileToken cannot have property declarations");
    }

    @Override
    protected void throwIfInvalidVisibility() {
        if (visibilityModifier != null) throw new CompilationException("SourceFileToken cannot have visibility modifiers");
    }
}
