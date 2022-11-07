package me.cometkaizo.temp.syntaxes;

import me.cometkaizo.temp.Parser;
import me.cometkaizo.temp.nodes.ClassNode;
import me.cometkaizo.temp.nodes.DeclaredNode;
import me.cometkaizo.temp.syntaxes.nodes.ConditionalSyntaxNodeBuilder;
import me.cometkaizo.temp.syntaxes.nodes.MatcherFunction;
import me.cometkaizo.temp.syntaxes.nodes.Syntax;
import me.cometkaizo.temp.syntaxes.nodes.TokenTypeSyntaxNodeBuilder;
import me.cometkaizo.wavetech.lexer.tokens.Token;
import me.cometkaizo.wavetech.lexer.tokens.TokenType;
import me.cometkaizo.wavetech.lexer.tokens.types.PrimitiveType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static me.cometkaizo.wavetech.lexer.tokens.types.ObjectType.*;
import static me.cometkaizo.wavetech.lexer.tokens.types.PrimitiveOperator.*;
import static me.cometkaizo.wavetech.lexer.tokens.types.PropertyModifier.*;
import static me.cometkaizo.wavetech.lexer.tokens.types.VisibilityModifier.*;

public class MethodDeclarationSyntax extends DeclarationSyntax {


    // (public|protected|private|)
    // (static)?
    // (abstract|final)?
    // (_primitive-type_|_object-type_|void)
    // _symbol_
    // (
    // ((_p_|_o_) _symbol_(, (_p_|_o_) _symbol_)*)?
    // )

    protected final List<TokenType> parameterTypes = new ArrayList<>();
    protected final List<String> parameterNames = new ArrayList<>();

    public MethodDeclarationSyntax() {

        // TODO: 2022-11-04 Store parameter types & names, and allow for single and no-arg methods

        rootNode
                .optionally(PUBLIC, PACKAGE_PRIVATE, PROTECTED, PRIVATE)
                .optionally(STATIC)
                .optionally(ABSTRACT, FINAL)
                .split(
                        new ConditionalSyntaxNodeBuilder(PrimitiveType.class), // includes void
                        new TokenTypeSyntaxNodeBuilder(SYMBOL_OR_REFERENCE, REFERENCE)
                )
                .then(SYMBOL_OR_REFERENCE, SYMBOL)
                .then(L_PAREN)
                // parameters
                .optionally(
                        new ConditionalSyntaxNodeBuilder(PrimitiveType.class), // includes void
                        new TokenTypeSyntaxNodeBuilder(SYMBOL_OR_REFERENCE, REFERENCE)
                )
                .then(SYMBOL_OR_REFERENCE, SYMBOL)/*
                .peekToken(new MatcherFunction() {
                    @NotNull
                    @Override
                    protected Syntax.Result match(@NotNull Token token, @Nullable Token nextToken) {
                        Result result = isComma(nextToken) ?
                                Result.MATCHES_SO_FAR :
                                Result.MATCHES_EXACT;
                        LogUtils.info("for token {} and next token {}, matches?");
                        return result;
                    }
                })*/
                .forEachNextToken(new MatcherFunction("parameters") {
                    @NotNull
                    @Override
                    protected Syntax.Result match(@NotNull Token token, @Nullable Token nextToken) {
                        Result matchResult = matchNextFor(ParameterSyntax::new, "parameter");
                        if (matchResult == Result.MATCHES_EXACT) {
                            if (!isComma(nextToken)) return Result.MATCHES_EXACT;
                            return Result.MATCHES_SO_FAR;
                        }
                        return matchResult;
                    }
                })
                .then(R_PAREN)
        ;

        addNextExpectedSyntax(new LeftBraceSyntax());

    }


    static class ParameterSyntax extends Syntax {

        public ParameterSyntax() {

            rootNode
                    .then(COMMA)
                    .split(
                            new ConditionalSyntaxNodeBuilder(PrimitiveType.class), // includes void
                            new TokenTypeSyntaxNodeBuilder(SYMBOL_OR_REFERENCE, REFERENCE)
                    )
                    .then(SYMBOL_OR_REFERENCE, SYMBOL)
            ;

        }

        @Override
        protected boolean isValidInStatus(Parser.Status status) {
            return true;
        }

        @Override
        protected boolean isValidInContext(DeclaredNode context) {
            return true;
        }
    }

    private boolean isComma(Token candidate) {
        if (candidate == null) return false;
        return candidate.getType().equals(COMMA);
    }

    @Override
    public boolean hasBody() {
        return true;
    }

    @Override
    protected boolean isValidInStatus(Parser.Status status) {
        return true;
    }

    @Override
    protected boolean isValidInContext(DeclaredNode context) {
        return context instanceof ClassNode;
    }
}
