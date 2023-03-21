package me.cometkaizo.wavetech.syntaxes;

import me.cometkaizo.util.PeekingIterator;
import me.cometkaizo.wavetech.lexer.tokens.Token;

import java.util.Set;

public abstract class ResultBearingSyntaxNode<T> extends SyntaxNode {
    protected final Set<String> resultLabels;

    protected ResultBearingSyntaxNode(ResultBearingSyntaxNodeBuilder<T> builder) {
        super(builder);
        this.resultLabels = builder.resultLabels;
    }

    @Override
    protected abstract boolean matchAndStore(PeekingIterator<Token> tokens, SyntaxStructure structure, SyntaxMatcher matcher);

    protected void store(T result, SyntaxStructure structure) {
        for (var label : resultLabels) {
            structure.store(label, result);
        }
    }

}
