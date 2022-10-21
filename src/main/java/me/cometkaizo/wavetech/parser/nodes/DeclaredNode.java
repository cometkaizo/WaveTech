package me.cometkaizo.wavetech.parser.nodes;

import me.cometkaizo.wavetech.lexer.CompilationException;
import me.cometkaizo.wavetech.lexer.tokens.types.PropertyDeclaration;
import me.cometkaizo.wavetech.lexer.tokens.types.PropertyModifier;
import me.cometkaizo.wavetech.lexer.tokens.types.VisibilityModifier;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class DeclaredNode extends Node {


    protected final String name;
    protected final DeclaredNode containingToken;
    protected final VisibilityModifier visibilityModifier;
    protected final VisibilityModifier effectiveVisibilityModifier;
    @NotNull
    protected final Set<PropertyModifier> propertyModifiers;
    @NotNull
    protected final Set<PropertyDeclaration> propertyDeclarations;
    protected final List<BinaryNode<?>> nodes = new ArrayList<>();

    protected DeclaredNode(DeclaredNode containingToken, VisibilityModifier visibilityModifier, @NotNull Set<PropertyModifier> propertyModifiers, String name, @NotNull Set<PropertyDeclaration> propertyDeclarations) {

        this.containingToken = containingToken;

        throwIfIncompatibleModifiers(propertyModifiers);

        this.name = name;
        this.propertyModifiers = new HashSet<>(propertyModifiers);
        this.propertyDeclarations = propertyDeclarations;
        this.visibilityModifier = visibilityModifier;
        throwIfInvalidModifiers();
        throwIfInvalidPropertyDeclaration();
        throwIfInvalidVisibility();

        this.effectiveVisibilityModifier = getEffectiveVisibility();

    }

    protected abstract VisibilityModifier getEffectiveVisibility();


    protected void throwIfIncompatibleModifiers(@NotNull Set<PropertyModifier> propertyModifiers) {
        for (PropertyModifier propertyModifier : propertyModifiers) {
            for (PropertyModifier propertyModifier1 : propertyModifiers) {
                if (propertyModifier.equals(propertyModifier1)) continue;
                if (!propertyModifier.isCompatibleWith(propertyModifier1))
                    throw new CompilationException("Illegal combination of modifiers '" + propertyModifier + "' and '" + propertyModifier1 + "'");
            }
        }
    }

    protected abstract void throwIfInvalidModifiers();
    protected abstract void throwIfInvalidPropertyDeclaration();
    protected abstract void throwIfInvalidVisibility();

    public <T> void addNode(NodeType type, T value) {
        nodes.add(new BinaryNode<>(type, value));
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{\n" +
                "  name=" + name +
                ",visibility=" + visibilityModifier +
                ",modifiers=" + propertyModifiers +
                ",propertyDeclarations=" + propertyDeclarations +
                ",\n  nodes=[\n" + nodes.stream()
                        .map(node -> node.toString().indent(4))
                        .collect(Collectors.joining(",\n")) +
                "  ]\n}";
    }

    public List<BinaryNode<?>> getNodes() {
        return nodes;
    }

    public VisibilityModifier getVisibilityModifier() {
        return visibilityModifier;
    }

    public VisibilityModifier getEffectiveVisibilityModifier() {
        return effectiveVisibilityModifier;
    }

    public @NotNull Set<PropertyModifier> getPropertyModifiers() {
        return propertyModifiers;
    }

    public @NotNull Set<PropertyDeclaration> getPropertyDeclarations() {
        return propertyDeclarations;
    }
}
