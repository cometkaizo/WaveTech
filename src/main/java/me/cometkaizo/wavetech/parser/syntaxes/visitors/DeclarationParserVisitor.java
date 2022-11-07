package me.cometkaizo.wavetech.parser.syntaxes.visitors;

import me.cometkaizo.wavetech.lexer.tokens.types.PropertyDeclaration;
import me.cometkaizo.wavetech.lexer.tokens.types.PropertyModifier;
import me.cometkaizo.wavetech.lexer.tokens.types.VisibilityModifier;
import me.cometkaizo.wavetech.parser.nodes.DeclaredNode;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public abstract class DeclarationParserVisitor extends ContainedParserVisitor {

    protected final VisibilityModifier visibilityModifier;
    @NotNull
    protected final Set<PropertyModifier> propertyModifiers;
    protected final String name;
    protected final Set<PropertyDeclaration> propertyDeclarations;

    public DeclarationParserVisitor(DeclaredNode containingToken,
                                    VisibilityModifier visibilityModifier,
                                    @NotNull Set<PropertyModifier> propertyModifiers,
                                    String name,
                                    @NotNull Set<PropertyDeclaration> propertyDeclarations) {
        super(containingToken);
        this.visibilityModifier = visibilityModifier;
        this.propertyModifiers = propertyModifiers;
        this.name = name;
        this.propertyDeclarations = propertyDeclarations;
    }

}
