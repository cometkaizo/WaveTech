package me.cometkaizo.temp.syntaxes.nodes;

import me.cometkaizo.util.LogUtils;

public class EmptySyntaxNode extends SoftSyntaxNode {

    protected EmptySyntaxNode(EmptySyntaxNodeBuilder builder) {
        super(builder);
    }

    @Override
    protected boolean accepts() {
        LogUtils.debug();
        return true;
    }

    @Override
    public String toString() {
        return "EmptyCommandNode{ }";
    }

    @Override
    protected String representData() {
        return "none";
    }
}
