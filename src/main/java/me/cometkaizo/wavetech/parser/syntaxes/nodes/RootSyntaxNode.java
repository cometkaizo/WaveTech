package me.cometkaizo.wavetech.parser.syntaxes.nodes;

class RootSyntaxNode extends SoftSyntaxNode {

    public RootSyntaxNode(RootSyntaxNodeBuilder builder) {
        super(builder);
    }


    @Override
    protected String representData() {
        return "ROOT";
    }

    @Override
    protected boolean accepts() {
        return true;
    }
}
