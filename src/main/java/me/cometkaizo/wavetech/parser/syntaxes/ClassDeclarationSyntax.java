package me.cometkaizo.wavetech.parser.syntaxes;

import me.cometkaizo.wavetech.lexer.tokens.Token;
import me.cometkaizo.wavetech.lexer.tokens.types.ObjectType;
import me.cometkaizo.wavetech.lexer.tokens.types.PropertyModifier;
import me.cometkaizo.wavetech.lexer.tokens.types.VisibilityModifier;
import me.cometkaizo.wavetech.parser.Parser;
import me.cometkaizo.wavetech.parser.nodes.DeclaredNode;
import me.cometkaizo.wavetech.parser.nodes.SourceFileNode;
import me.cometkaizo.wavetech.parser.syntaxes.nodes.TokenTypeSyntaxNodeBuilder;
import me.cometkaizo.wavetech.parser.syntaxes.visitors.ClassDeclarationParserVisitor;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static me.cometkaizo.wavetech.lexer.tokens.types.DeclarationKeyword.CLASS;
import static me.cometkaizo.wavetech.lexer.tokens.types.ObjectType.SYMBOL;
import static me.cometkaizo.wavetech.lexer.tokens.types.ObjectType.SYMBOL_OR_REFERENCE;
import static me.cometkaizo.wavetech.lexer.tokens.types.PropertyModifier.*;
import static me.cometkaizo.wavetech.lexer.tokens.types.VisibilityModifier.PACKAGE_PRIVATE;
import static me.cometkaizo.wavetech.lexer.tokens.types.VisibilityModifier.PUBLIC;

public class ClassDeclarationSyntax extends DeclarationSyntax {

    // (public|) ((sealed( abstract)?)|abstract|final|) class

    public ClassDeclarationSyntax() {

        rootNode
                .optionally(PUBLIC, PACKAGE_PRIVATE)
                .optionally(
                        new TokenTypeSyntaxNodeBuilder(SEALED).optionally(ABSTRACT),
                        new TokenTypeSyntaxNodeBuilder(ABSTRACT),
                        new TokenTypeSyntaxNodeBuilder(FINAL)
                ).then(CLASS)
                .then(SYMBOL_OR_REFERENCE, SYMBOL)
        ;

        addNextExpectedSyntax(new LeftBraceSyntax());
    }

    @Override
    public boolean hasBody() {
        return true;
    }

    @Override
    public ClassDeclarationParserVisitor createVisitor(DeclaredNode containingToken) {
        var inputs = getInputTokens();

        List<Token> visibilityModifierList = inputs.get(VisibilityModifier.class);
        VisibilityModifier visibilityModifier = visibilityModifierList == null ?
                PACKAGE_PRIVATE :
                (VisibilityModifier) visibilityModifierList.get(0).getType();

        List<Token> propertyModifierList = inputs.get(PropertyModifier.class);
        Set<PropertyModifier> propertyModifiers = propertyModifierList == null ?
                Set.of() :
                propertyModifierList.stream()
                        .map(token -> (PropertyModifier) token.getType()).collect(Collectors.toSet());

        String name = (String) inputs.get(ObjectType.class).stream()
                .filter(token -> token.getType() == SYMBOL).findFirst()
                .orElseThrow().getValue();

        return new ClassDeclarationParserVisitor(
                containingToken,
                visibilityModifier,
                propertyModifiers,
                name,
                Set.of());
    }

    @Override
    protected boolean isValidInStatus(Parser.Status status) {
        return true;
    }

    @Override
    protected boolean isValidInContext(DeclaredNode context) {
        return context instanceof SourceFileNode;
    }

}
