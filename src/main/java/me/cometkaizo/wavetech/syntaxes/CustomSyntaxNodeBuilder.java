package me.cometkaizo.wavetech.syntaxes;

import me.cometkaizo.util.NullableOptional;
import me.cometkaizo.util.PeekingIterator;
import me.cometkaizo.util.TriFunction;
import me.cometkaizo.wavetech.lexer.tokens.Token;
import org.jetbrains.annotations.NotNull;

public class CustomSyntaxNodeBuilder<T> extends ResultBearingSyntaxNodeBuilder<T> {

    @NotNull
    protected final TriFunction<PeekingIterator<Token>, SyntaxStructure, SyntaxMatcher, @NotNull NullableOptional<T>> resultFunction;

    public CustomSyntaxNodeBuilder(@NotNull TriFunction<PeekingIterator<Token>, SyntaxStructure, SyntaxMatcher, @NotNull NullableOptional<T>> resultFunction) {
        this.resultFunction = resultFunction;
    }

    @Override
    protected CustomSyntaxNode<T> build() {
        return new CustomSyntaxNode<>(this);
    }
}
