package me.cometkaizo.wavetech.syntaxes;

public class EmptySyntaxNodeBuilder extends SoftSyntaxNodeBuilder {

    @Override
    protected EmptySyntaxNode build() {
        return new EmptySyntaxNode(this);
    }

    @Override
    public String toString() {
        return "EmptySyntaxNodeBuilder{ }";
    }
}
