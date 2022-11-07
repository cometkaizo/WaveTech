package me.cometkaizo.temp.syntaxes.nodes;

class EmptySyntaxNodeBuilder extends SoftSyntaxNodeBuilder {

    @Override
    protected EmptySyntaxNode build() {
        return new EmptySyntaxNode(this);
    }

    @Override
    public String toString() {
        return "EmptySyntaxNodeBuilder{ }";
    }
}
