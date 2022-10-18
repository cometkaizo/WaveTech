package me.cometkaizo.wavetech.parser.syntaxparseres;

import me.cometkaizo.wavetech.lexer.tokens.Token;
import me.cometkaizo.wavetech.lexer.tokens.types.ObjectType;
import me.cometkaizo.wavetech.lexer.tokens.types.PropertyModifier;
import me.cometkaizo.wavetech.lexer.tokens.types.VisibilityModifier;
import me.cometkaizo.wavetech.parser.Parser;
import me.cometkaizo.wavetech.parser.nodes.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static me.cometkaizo.wavetech.lexer.tokens.types.DeclarationKeyword.CLASS;
import static me.cometkaizo.wavetech.lexer.tokens.types.ObjectType.SYMBOL;
import static me.cometkaizo.wavetech.lexer.tokens.types.ObjectType.SYMBOL_OR_REFERENCE;
import static me.cometkaizo.wavetech.lexer.tokens.types.PrimitiveOperator.L_BRACE;
import static me.cometkaizo.wavetech.lexer.tokens.types.PropertyModifier.*;
import static me.cometkaizo.wavetech.lexer.tokens.types.VisibilityModifier.PACKAGE_PRIVATE;
import static me.cometkaizo.wavetech.lexer.tokens.types.VisibilityModifier.PUBLIC;

// TODO: 2022-10-16 Refactor SyntaxParser to use nodes like commands
public class ClassDeclarationSyntaxParser extends DeclarationSyntaxParser {

    // (public|) class

    public ClassDeclarationSyntaxParser() {
        addExpectedSyntax(
                new AnyMatcher(true, PUBLIC, PACKAGE_PRIVATE),
                new AnyMatcher(true, SEALED, FINAL),
                CLASS,
                new TokenTypeTransformationMatcher(SYMBOL_OR_REFERENCE, SYMBOL, new AnyMatcher(false, SYMBOL))
        );
        addExpectedSyntax(
                new AnyMatcher(true, PUBLIC, PACKAGE_PRIVATE),
                new AnyMatcher(true, SEALED),
                new AnyMatcher(true, ABSTRACT),
                CLASS,
                new TokenTypeTransformationMatcher(SYMBOL_OR_REFERENCE, SYMBOL, new AnyMatcher(false, SYMBOL))
        );

        addNextExpectedSyntax(new SyntaxParser() {
            {
                addExpectedSyntax(
                        L_BRACE
                );
            }

            @Override
            protected boolean isValidInStatus(Parser.ParserStatus status) {
                return true;
            }

            @Override
            protected boolean isValidInContext(DeclaredNode context) {
                return true;
            }
        });
    }

    @Override
    public Node create(DeclaredNode containingToken) {
        var inputs = getExactMatchingInputPattern();

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

        return new ClassNode(
                containingToken,
                visibilityModifier,
                propertyModifiers,
                name,
                Set.of()
        );
    }

    @Override
    public NodeType getOperationType() {
        return NodeType.CLASS_DECLARATION;
    }

    @Override
    public boolean hasBody() {
        return true;
    }

    @Override
    protected boolean isValidInStatus(Parser.ParserStatus status) {
        return true;
    }

    @Override
    protected boolean isValidInContext(DeclaredNode context) {
        return context instanceof SourceFileNode;
    }

}
