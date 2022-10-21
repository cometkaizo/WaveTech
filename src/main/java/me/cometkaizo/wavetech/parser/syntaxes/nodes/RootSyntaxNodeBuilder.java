package me.cometkaizo.wavetech.parser.syntaxes.nodes;

public class RootSyntaxNodeBuilder extends SyntaxNodeBuilder {

    public RootSyntaxNodeBuilder(Syntax context) {
        this.syntax = context;
    }

    @Override
    protected RootSyntaxNode build() {
        return new RootSyntaxNode(this);
    }

    @Override
    public String toString() {
        return "RootSyntaxNodeBuilder{ }";
    }
}
