package me.cometkaizo.wavetech.parser.syntaxes.nodes;

import me.cometkaizo.wavetech.lexer.tokens.Token;

/**
 * Represents classes that do not need an argument to determine its functionality
 */
abstract class SoftSyntaxNode extends SyntaxNode {

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
}
