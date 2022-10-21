package me.cometkaizo.wavetech.parser.nodes;

import me.cometkaizo.wavetech.lexer.CompilationException;
import me.cometkaizo.wavetech.lexer.tokens.Token;
import me.cometkaizo.wavetech.lexer.tokens.types.*;
import me.cometkaizo.wavetech.parser.UnexpectedTokenException;

import java.util.Set;
import java.util.stream.Collectors;

public class FieldNode extends MemberNode {

    private final Token type;

    public FieldNode(DeclaredNode containingToken, VisibilityModifier visibilityModifier, Set<PropertyModifier> propertyModifiers, Token type, String name, Set<PropertyDeclaration> propertyDeclaration) {
        super(containingToken, visibilityModifier, propertyModifiers, name, propertyDeclaration);
        this.type = type;

        throwIfInvalidType();
    }

    protected void throwIfInvalidType() {
        if (type == null ||
                (!(type.getType() instanceof PrimitiveType) && !(type.getType() instanceof ObjectType)) ||
                type.getType() instanceof PrimitiveType && type.getType() == PrimitiveType.VOID)
            throw new UnexpectedTokenException("Illegal type '" + type + "for FieldNode");
    }

    @Override
    protected void throwIfInvalidModifiers() {
        for (PropertyModifier propertyModifier : propertyModifiers) {
            switch (propertyModifier) {
                case STATIC, FINAL, TRANSIENT, VOLATILE -> {}
                default -> throw new CompilationException("Modifier '" + propertyModifier + "' not allowed here");
            }
        }
    }

    @Override
    protected void throwIfInvalidPropertyDeclaration() {
        if (!propertyDeclarations.isEmpty()) throw new CompilationException("Property declaration '" + propertyDeclarations.stream().findAny() + "' not allowed here");
    }

    @Override
    protected void throwIfInvalidVisibility() {

    }

    public Token getType() {
        return type;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
                "name=" + name +
                ",visibility=" + visibilityModifier +
                ",modifiers=" + propertyModifiers +
                ",type=" + type +
                ",propertyDeclarations=" + propertyDeclarations +
                ",nodes=" + nodes.stream()
                        .map(binaryNode -> binaryNode.toString().indent(2))
                        .collect(Collectors.joining(",\n", "[", "]")) +
                '}';
    }
}
