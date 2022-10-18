package me.cometkaizo.wavetech.parser.syntaxparseres;

import me.cometkaizo.wavetech.lexer.tokens.Token;
import me.cometkaizo.wavetech.lexer.tokens.types.ObjectType;
import me.cometkaizo.wavetech.lexer.tokens.types.PrimitiveType;
import me.cometkaizo.wavetech.lexer.tokens.types.PropertyModifier;
import me.cometkaizo.wavetech.lexer.tokens.types.VisibilityModifier;
import me.cometkaizo.wavetech.parser.Parser;
import me.cometkaizo.wavetech.parser.nodes.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static me.cometkaizo.wavetech.lexer.tokens.types.ObjectType.*;
import static me.cometkaizo.wavetech.lexer.tokens.types.PrimitiveOperator.SEMICOLON;
import static me.cometkaizo.wavetech.lexer.tokens.types.PrimitiveType.VOID;
import static me.cometkaizo.wavetech.lexer.tokens.types.PropertyModifier.*;
import static me.cometkaizo.wavetech.lexer.tokens.types.VisibilityModifier.*;

public class FieldDeclarationSyntaxParser extends DeclarationSyntaxParser {

    public FieldDeclarationSyntaxParser() {
        addExpectedSyntax(
                new AnyMatcher(true, PUBLIC, PROTECTED, PACKAGE_PRIVATE, PRIVATE),
                new AnyMatcher(true, STATIC),
                new AnyMatcher(true, FINAL, VOLATILE),
                new AnyMatcher(true, TRANSIENT),
                new TokenTypeTransformationMatcher(ObjectType.SYMBOL_OR_REFERENCE, ObjectType.REFERENCE),
                new TokenTypeTransformationMatcher(ObjectType.SYMBOL_OR_REFERENCE, ObjectType.SYMBOL)
        );
        addExpectedSyntax(
                new AnyMatcher(true, PUBLIC, PROTECTED, PACKAGE_PRIVATE, PRIVATE),
                new AnyMatcher(true, STATIC),
                new AnyMatcher(true, FINAL, VOLATILE),
                new AnyMatcher(true, TRANSIENT),
                new PredicateAnyMatcher(o -> o.getType() instanceof PrimitiveType && o.getType() != VOID),
                new TokenTypeTransformationMatcher(SYMBOL_OR_REFERENCE, SYMBOL)
        );

        addNextExpectedSyntax(
                new SyntaxParser() {
                    {
                        addExpectedSyntax(
                                SEMICOLON
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
                }
        );
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

        boolean isPrimitive = inputs.containsKey(PrimitiveType.class);

        Token type = isPrimitive?
                inputs.get(PrimitiveType.class).get(0) :
                inputs.get(ObjectType.class).stream()
                        .filter(token -> token.getType() == SYMBOL).findFirst()
                        .orElseThrow();

        String name = (String) inputs.get(ObjectType.class).stream()
                .filter(token -> token.getType() == SYMBOL).findFirst()
                .orElseThrow().getValue();


        return new FieldNode(
                containingToken,
                visibilityModifier,
                propertyModifiers,
                type,
                name,
                Set.of()
        );
    }

    @Override
    public NodeType getOperationType() {
        return NodeType.FIELD_DECLARATION;
    }

    @Override
    public boolean hasBody() {
        return false;
    }



    @Override
    protected boolean isValidInStatus(Parser.ParserStatus status) {
        return true;
    }

    @Override
    protected boolean isValidInContext(DeclaredNode context) {
        return context instanceof ClassNode;
    }
}
