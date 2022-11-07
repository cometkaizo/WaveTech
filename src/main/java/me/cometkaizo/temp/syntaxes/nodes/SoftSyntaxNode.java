package me.cometkaizo.temp.syntaxes.nodes;

import me.cometkaizo.wavetech.lexer.tokens.Token;

/**
 * Represents classes that do not need an argument to determine its functionality
 */
abstract class SoftSyntaxNode extends SyntaxNode {

    protected final int hashCode = (int) (Math.random() * 100 * 31 * getClass().hashCode());

    protected SoftSyntaxNode(SoftSyntaxNodeBuilder builder) {
        super(builder);
    }

    @Override
    protected final boolean accepts(Token nextToken) {
        return accepts();
    }

    @Override
    protected final Token transform(Token nextToken) {
        throw new UnsupportedOperationException();
    }

    protected abstract boolean accepts();

    @Override
    public boolean equals(Object o) {
        return this == o;
    }

    @Override
    public int hashCode() {
        return hashCode;
    }
}
