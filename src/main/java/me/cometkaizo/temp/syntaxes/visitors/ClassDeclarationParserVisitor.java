package me.cometkaizo.temp.syntaxes.visitors;

import me.cometkaizo.temp.Parser;
import me.cometkaizo.temp.nodes.ClassDeclarationNode;
import me.cometkaizo.temp.nodes.ClassNode;
import me.cometkaizo.temp.nodes.DeclaredNode;
import me.cometkaizo.wavetech.lexer.tokens.types.PropertyDeclaration;
import me.cometkaizo.wavetech.lexer.tokens.types.PropertyModifier;
import me.cometkaizo.wavetech.lexer.tokens.types.VisibilityModifier;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class ClassDeclarationParserVisitor extends DeclarationParserVisitor {

    public ClassDeclarationParserVisitor(DeclaredNode containingToken,
                                         VisibilityModifier visibilityModifier,
                                         @NotNull Set<PropertyModifier> propertyModifiers,
                                         String name,
                                         @NotNull Set<PropertyDeclaration> propertyDeclarations) {
        super(containingToken, visibilityModifier, propertyModifiers, name, propertyDeclarations);
    }

    @Override
    public void visit(Parser parser) {
        ClassNode classNode = new ClassNode(
                containingToken,
                visibilityModifier,
                propertyModifiers,
                name,
                Set.of()
        );
        containingToken.addNode(new ClassDeclarationNode(classNode));
        parser.parseInContext(classNode);
    }
}
