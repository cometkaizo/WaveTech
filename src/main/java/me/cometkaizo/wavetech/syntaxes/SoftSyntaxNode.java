package me.cometkaizo.wavetech.syntaxes;

import me.cometkaizo.util.PeekingIterator;
import me.cometkaizo.wavetech.lexer.tokens.Token;

/**
 * Represents node types that do not need tokens to determine its functionality
 */
abstract class SoftSyntaxNode extends SyntaxNode {

    protected final int hashCode = (int) (Math.random() * 100 * 31 * getClass().hashCode());

    protected SoftSyntaxNode(SoftSyntaxNodeBuilder builder) {
        super(builder);
    }

    @Override
    protected final boolean matchAndStore(PeekingIterator<Token> tokens, SyntaxStructure structure, SyntaxMatcher matcher) {
        return matches();
    }

    protected abstract boolean matches();

    @Override
    public boolean equals(Object o) {
        return this == o;
    }

    @Override
    public int hashCode() {
        return hashCode;
    }
}
