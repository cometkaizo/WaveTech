package me.cometkaizo.temp.nodes;

import me.cometkaizo.wavetech.lexer.tokens.types.PropertyDeclaration;
import me.cometkaizo.wavetech.lexer.tokens.types.PropertyModifier;
import me.cometkaizo.wavetech.lexer.tokens.types.VisibilityModifier;

import java.util.Set;

public abstract class MemberNode extends DeclaredNode {

    protected MemberNode(DeclaredNode containingToken, VisibilityModifier visibilityModifier, Set<PropertyModifier> propertyModifiers, String name, Set<PropertyDeclaration> propertyDeclaration) {
        super(containingToken, visibilityModifier, propertyModifiers, name, propertyDeclaration);
    }

    @Override
    protected VisibilityModifier getEffectiveVisibility() {
        if (containingToken.effectiveVisibilityModifier.power() < visibilityModifier.power())
            return containingToken.effectiveVisibilityModifier;
        return visibilityModifier;
    }

}
