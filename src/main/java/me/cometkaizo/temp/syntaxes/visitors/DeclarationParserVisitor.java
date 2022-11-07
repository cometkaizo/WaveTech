package me.cometkaizo.temp.syntaxes.visitors;

import me.cometkaizo.temp.nodes.DeclaredNode;
import me.cometkaizo.wavetech.lexer.tokens.types.PropertyDeclaration;
import me.cometkaizo.wavetech.lexer.tokens.types.PropertyModifier;
import me.cometkaizo.wavetech.lexer.tokens.types.VisibilityModifier;
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
