package me.cometkaizo.wavetech.syntaxes;

class RootSyntaxNode extends SoftSyntaxNode {

    public RootSyntaxNode(RootSyntaxNodeBuilder builder) {
        super(builder);
    }


    @Override
    public String toPrettyString() {
        return "ROOT";
    }

    @Override
    protected boolean matches() {
        return true;
    }
}
