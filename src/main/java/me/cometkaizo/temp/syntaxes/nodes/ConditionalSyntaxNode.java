package me.cometkaizo.temp.syntaxes.nodes;

import me.cometkaizo.util.LogUtils;
import me.cometkaizo.wavetech.lexer.tokens.Token;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.Function;

class ConditionalSyntaxNode extends SyntaxNode {

    @NotNull
    protected final Function<Token, Boolean> condition;

    public ConditionalSyntaxNode(ConditionalSyntaxNodeBuilder builder) {
        super(builder);
        this.condition = builder.condition;
    }

    @Override
    protected Token transform(Token nextToken) {
        return nextToken;
    }

    @Override
    protected boolean accepts(Token nextToken) {
        return condition.apply(nextToken);
    }

    @Override
    protected String representData() {
        return "CONDITIONAL";
    }

    @Override
    public String toString() {
        return LogUtils.withArgs("""
                ConditionalCommandNode{
                    condition: {},
                }""", condition);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConditionalSyntaxNode that = (ConditionalSyntaxNode) o;
        return condition.equals(that.condition);
    }

    @Override
    public int hashCode() {
        return Objects.hash(condition);
    }
}
