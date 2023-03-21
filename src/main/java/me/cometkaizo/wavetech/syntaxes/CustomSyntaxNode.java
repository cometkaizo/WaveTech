package me.cometkaizo.wavetech.syntaxes;

import me.cometkaizo.util.NullableOptional;
import me.cometkaizo.util.PeekingIterator;
import me.cometkaizo.util.TriFunction;
import me.cometkaizo.wavetech.lexer.tokens.Token;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class CustomSyntaxNode<T> extends ResultBearingSyntaxNode<T> {

    @NotNull
    protected final TriFunction<PeekingIterator<Token>, SyntaxStructure, SyntaxMatcher, NullableOptional<T>> resultFunction;

    protected CustomSyntaxNode(CustomSyntaxNodeBuilder<T> builder) {
        super(builder);
        this.resultFunction = builder.resultFunction;
    }

    @Override
    protected boolean matchAndStore(PeekingIterator<Token> tokens, SyntaxStructure structure, SyntaxMatcher matcher) {
        var result = resultFunction.apply(tokens, structure, matcher);
        if (result.isPresent()) store(result.orElseThrow(), structure);
        return result.isPresent();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustomSyntaxNode<?> that = (CustomSyntaxNode<?>) o;
        return Objects.equals(resultFunction, that.resultFunction);
    }

    @Override
    public int hashCode() {
        return Objects.hash(resultFunction);
    }

    @Override
    public String toPrettyString() {
        return "CUSTOM";
    }

}
