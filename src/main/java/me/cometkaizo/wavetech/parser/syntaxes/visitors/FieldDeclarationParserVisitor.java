package me.cometkaizo.wavetech.parser.syntaxes.visitors;

import me.cometkaizo.wavetech.lexer.tokens.Token;
import me.cometkaizo.wavetech.lexer.tokens.types.PropertyDeclaration;
import me.cometkaizo.wavetech.lexer.tokens.types.PropertyModifier;
import me.cometkaizo.wavetech.lexer.tokens.types.VisibilityModifier;
import me.cometkaizo.wavetech.parser.Parser;
import me.cometkaizo.wavetech.parser.nodes.*;

import java.util.Set;

public class FieldDeclarationParserVisitor extends DeclarationParserVisitor {

    protected final Token type;

    public FieldDeclarationParserVisitor(DeclaredNode containingToken, VisibilityModifier visibilityModifier, Set<PropertyModifier> propertyModifiers, Token type, String name, Set<PropertyDeclaration> propertyDeclarations) {
        super(containingToken, visibilityModifier, propertyModifiers, name, propertyDeclarations);
        this.type = type;
    }

    @Override
    public void visit(Parser parser) {
        FieldNode fieldNode = new FieldNode(
                containingToken,
                visibilityModifier,
                propertyModifiers,
                type,
                name,
                Set.of()
        );
        containingToken.addNode(new FieldDeclarationNode(fieldNode));
    }
}
