package me.cometkaizo.commands.nodes;

import me.cometkaizo.util.LogUtils;

class EmptyCommandNode extends SoftCommandNode {

    protected EmptyCommandNode(EmptyCommandNodeBuilder builder) {
        super(builder);
    }

    @Override
    protected boolean accepts() {
        return true;
    }

    @Override
    public String toString() {
        return LogUtils.withArgs("""
                EmptyCommandNode{
                    level: {}
                }""", level);
    }
}
