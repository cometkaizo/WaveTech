package me.cometkaizo.temp.nodes;

import me.cometkaizo.wavetech.lexer.CompilationException;
import me.cometkaizo.wavetech.lexer.tokens.types.PermitsPropertyDeclaration;
import me.cometkaizo.wavetech.lexer.tokens.types.PropertyDeclaration;
import me.cometkaizo.wavetech.lexer.tokens.types.PropertyModifier;
import me.cometkaizo.wavetech.lexer.tokens.types.VisibilityModifier;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class ClassNode extends DeclaredNode {

    public ClassNode(DeclaredNode containingToken, VisibilityModifier visibility, @NotNull Set<PropertyModifier> propertyModifiers, String name, @NotNull Set<PropertyDeclaration> propertyDeclaration) {
        super(containingToken, visibility, propertyModifiers, name, propertyDeclaration);
    }

    @Override
    protected VisibilityModifier getEffectiveVisibility() {
        return visibilityModifier;
    }

    @Override
    protected void throwIfInvalidModifiers() {
        boolean contextIsSourceFile = containingToken instanceof SourceFileNode;

        Set<PropertyModifier> allowedModifiers = contextIsSourceFile ?
                Set.of(PropertyModifier.FINAL, PropertyModifier.SEALED, PropertyModifier.ABSTRACT) :
                Set.of(PropertyModifier.FINAL, PropertyModifier.SEALED, PropertyModifier.ABSTRACT, PropertyModifier.STATIC);

        for (PropertyModifier propertyModifier : propertyModifiers) {
            if (!allowedModifiers.contains(propertyModifier))
                throw new CompilationException("Property modifier '" + propertyModifier + "' not allowed here");
        }

        boolean isSealed = propertyModifiers.contains(PropertyModifier.SEALED);
        boolean hasPermitsClause = propertyDeclarations.stream().anyMatch(o -> o instanceof PermitsPropertyDeclaration);
        if (isSealed && !hasPermitsClause)
            throw new CompilationException("Sealed class '" + name + "' must have permits clause");

    }

    @Override
    protected void throwIfInvalidPropertyDeclaration() {
        boolean isSealed = propertyModifiers.contains(PropertyModifier.SEALED);
        boolean hasPermitsClause = propertyDeclarations.stream().anyMatch(pd -> pd instanceof PermitsPropertyDeclaration);

        if (hasPermitsClause && !isSealed)
            throw new CompilationException("Unsealed class '" + name + "' must not have permits clause");
    }

    @Override
    protected void throwIfInvalidVisibility() {
        if (visibilityModifier == null) throw new CompilationException("Visibility modifier cannot be null");
        if (containingToken instanceof SourceFileNode) {
            if (visibilityModifier != VisibilityModifier.PUBLIC &&
                    visibilityModifier != VisibilityModifier.PACKAGE_PRIVATE)
                throw new CompilationException("Non inner-class cannot have '" + visibilityModifier + "' visibility");
        }
    }
}
