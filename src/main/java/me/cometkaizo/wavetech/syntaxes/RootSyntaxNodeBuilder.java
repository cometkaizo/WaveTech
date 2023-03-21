package me.cometkaizo.wavetech.syntaxes;

public class RootSyntaxNodeBuilder extends SoftSyntaxNodeBuilder {

    public RootSyntaxNodeBuilder(Syntax<?> syntax) {
        addSyntax(syntax);
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
