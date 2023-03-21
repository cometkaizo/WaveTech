package me.cometkaizo.wavetech.syntaxes;

public class AnchorSyntaxNodeBuilder extends SyntaxNodeBuilder {

    protected final int anchor;

    public AnchorSyntaxNodeBuilder(int anchor) {
        this.anchor = anchor;
    }

    @Override
    protected AnchorSyntaxNode build() {
        return new AnchorSyntaxNode(this);
    }
}
