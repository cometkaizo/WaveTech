package me.cometkaizo.wavetech.syntaxes;

import me.cometkaizo.logging.LogUtils;
import me.cometkaizo.util.PeekingIterator;
import me.cometkaizo.wavetech.lexer.tokens.Token;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.Predicate;

class ConditionalSyntaxNode extends ResultBearingSyntaxNode<Token> {

    @NotNull
    protected final Predicate<Token> condition;

    public ConditionalSyntaxNode(ConditionalSyntaxNodeBuilder builder) {
        super(builder);
        this.condition = builder.condition;
    }

    @Override
    protected boolean matchAndStore(PeekingIterator<Token> tokens, SyntaxStructure structure, SyntaxMatcher matcher) {
        if (!tokens.hasNext()) return false;
        var token = tokens.next();

        if (condition.test(token)) {
            store(token, structure);
            return true;
        }

        return false;
    }

    @Override
    public String toPrettyString() {
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
