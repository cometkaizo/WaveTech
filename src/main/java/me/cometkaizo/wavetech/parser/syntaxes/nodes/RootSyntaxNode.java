package me.cometkaizo.wavetech.parser.syntaxes.nodes;

import me.cometkaizo.wavetech.lexer.tokens.Token;

class RootSyntaxNode extends SyntaxNode {

    public RootSyntaxNode(RootSyntaxNodeBuilder builder) {
        super(builder);
    }


    @Override
    protected boolean accepts(Token nextToken) {
        return true;
    }

    @Override
    protected String representData() {
        return "ROOT";
    }

    @Override
    protected Token transform(Token nextToken) {
        return nextToken;
    }
}
