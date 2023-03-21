package me.cometkaizo.wavetech.syntaxes;

public class EmptySyntaxNode extends SoftSyntaxNode {

    protected EmptySyntaxNode(EmptySyntaxNodeBuilder builder) {
        super(builder);
    }

    @Override
    protected boolean matches() {
        return true;
    }

    @Override
    public String toString() {
        return "EmptySyntaxNode{ }";
    }

    @Override
    public String toPrettyString() {
        return "none";
    }
}
