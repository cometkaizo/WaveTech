package me.cometkaizo.wavetech.syntaxes;

import me.cometkaizo.util.PeekingIterator;
import me.cometkaizo.wavetech.lexer.tokens.Token;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

class SyntaxSyntaxNode<T extends SyntaxStructure> extends ResultBearingSyntaxNode<SyntaxStructure> {

    @NotNull
    protected final Syntax<? extends T> syntax;

    protected SyntaxSyntaxNode(SyntaxSyntaxNodeBuilder<T> builder) {
        super(builder);
        this.syntax = builder.syntaxSupplier.get();
    }

    protected boolean matchAndStore(PeekingIterator<Token> tokens, SyntaxStructure structure, SyntaxMatcher matcher) {
        SyntaxStructure result = matcher.tryMatch(syntax, structure, null);

        if (result == null) return false;

        store(result, structure);
        return true;
    }

    @Override
    public String toPrettyString() {
        return "SYNTAX: " + syntax.getClass().getSimpleName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SyntaxSyntaxNode<?> that = (SyntaxSyntaxNode<?>) o;
        return syntax.equals(that.syntax);
    }

    @Override
    public int hashCode() {
        return Objects.hash(syntax);
    }
}
