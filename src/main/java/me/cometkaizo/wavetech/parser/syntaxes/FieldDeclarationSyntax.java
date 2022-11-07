package me.cometkaizo.wavetech.parser.syntaxes;

import me.cometkaizo.util.LogUtils;
import me.cometkaizo.wavetech.lexer.tokens.Token;
import me.cometkaizo.wavetech.lexer.tokens.types.*;
import me.cometkaizo.wavetech.parser.Parser;
import me.cometkaizo.wavetech.parser.nodes.ClassNode;
import me.cometkaizo.wavetech.parser.nodes.DeclaredNode;
import me.cometkaizo.wavetech.parser.syntaxes.nodes.ConditionalSyntaxNodeBuilder;
import me.cometkaizo.wavetech.parser.syntaxes.nodes.TokenTypeSyntaxNodeBuilder;
import me.cometkaizo.wavetech.parser.syntaxes.visitors.FieldDeclarationParserVisitor;
import me.cometkaizo.wavetech.parser.syntaxes.visitors.ParserStatusVisitor;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static me.cometkaizo.wavetech.lexer.tokens.types.ObjectType.*;
import static me.cometkaizo.wavetech.lexer.tokens.types.PrimitiveType.VOID;
import static me.cometkaizo.wavetech.lexer.tokens.types.PropertyModifier.*;
import static me.cometkaizo.wavetech.lexer.tokens.types.VisibilityModifier.*;

public class FieldDeclarationSyntax extends DeclarationSyntax {

    public FieldDeclarationSyntax() {

        rootNode
                .executes(() -> LogUtils.debug("----------------0"))
                .optionally(PUBLIC, PROTECTED, PACKAGE_PRIVATE, PRIVATE)
                .executes(() -> LogUtils.debug("----------------1"))
                .optionally(STATIC)
                .executes(() -> LogUtils.debug("----------------2"))
                .optionally(FINAL, VOLATILE)
                .executes(() -> LogUtils.debug("----------------3"))
                .split(
                        new TokenTypeSyntaxNodeBuilder(SYMBOL_OR_REFERENCE, REFERENCE),
                        new ConditionalSyntaxNodeBuilder(token -> token.getType() instanceof PrimitiveType && token.getType() != VOID)
                )
                .executes(() -> LogUtils.debug("----------------4"))
                .then(SYMBOL_OR_REFERENCE, SYMBOL)
                .executes(() -> {
                    LogUtils.debug("----------------5");
                    FieldDeclarationSyntax.this.addNextExpectedSyntax(
                            new VariableAssignationSyntax(
                                    (String) getInputTokens().get(ObjectType.class).stream()
                                            .filter(token -> token.getType() == SYMBOL)
                                            .findFirst().orElseThrow()
                                            .getValue()
                            )
                    );
                })
                .failIfNextToken(nextToken ->
                        nextToken.getType().equals(PrimitiveOperator.L_PAREN)
                )
        ;

        addNextExpectedSyntax(
                new SemicolonSyntax()
        );
    }

    @Override
    public boolean hasBody() {
        return false;
    }

    @Override
    public ParserStatusVisitor createVisitor(DeclaredNode containingToken) {
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

        boolean isPrimitive = inputs.containsKey(PrimitiveType.class);
        if (!isPrimitive && !inputs.containsKey(ObjectType.class))
            throw new IllegalStateException("No type for field found: " + inputs);

        Token type = isPrimitive?
                inputs.get(PrimitiveType.class).get(0) :
                inputs.get(ObjectType.class).stream()
                        .filter(token -> token.getType() == SYMBOL).findFirst()
                        .orElseThrow();

        String name = (String) inputs.get(ObjectType.class).stream()
                .filter(token -> token.getType() == SYMBOL).findFirst()
                .orElseThrow().getValue();

        return new FieldDeclarationParserVisitor(
                containingToken,
                visibilityModifier,
                propertyModifiers,
                type,
                name,
                Set.of()
        );
    }

    @Override
    protected boolean isValidInStatus(Parser.Status status) {
        return true;
    }

    @Override
    protected boolean isValidInContext(DeclaredNode context) {
        boolean result = context instanceof ClassNode;
        LogUtils.debug("is valid in {}? {}", context, result);
        return result;
    }


}
