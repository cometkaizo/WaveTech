package me.cometkaizo.temp.syntaxes.nodes;

import me.cometkaizo.temp.Parser;
import me.cometkaizo.temp.nodes.DeclaredNode;
import me.cometkaizo.wavetech.lexer.tokens.Token;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

class LoopingSyntaxNode extends SyntaxNode {

    @NotNull
    protected final MatcherFunction function;

    protected LoopingSyntaxNode(LoopingSyntaxNodeBuilder builder) {
        super(builder);
        this.function = builder.function;
    }

    protected Syntax.Result checkNext(Token token, Token nextToken, DeclaredNode context, Parser.Status status) {
        return function.match(token, nextToken, context, status);
    }


    @Override
    protected Token transform(Token nextToken) {
        throw new AssertionError();
    }

    @Override
    protected boolean accepts(Token candidate) {
        throw new AssertionError();
    }

    @Override
    protected String representData() {
        return "LOOP$" + function.name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LoopingSyntaxNode that = (LoopingSyntaxNode) o;
        return function.equals(that.function);
    }

    @Override
    public int hashCode() {
        return Objects.hash(function);
    }
}
