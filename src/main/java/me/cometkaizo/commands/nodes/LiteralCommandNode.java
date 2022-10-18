package me.cometkaizo.commands.nodes;

import me.cometkaizo.util.LogUtils;

class LiteralCommandNode extends CommandNode {

    private final String literal;

    protected boolean accepts(String string) {
        LogUtils.debug("string: {}, literal: {}, equals? {}", string, literal, string.equals(literal));
        return string.equals(literal);
    }

    @Override
    protected void executeFunctionality() {

    }

    public LiteralCommandNode(LiteralCommandNodeBuilder builder) {
        super(builder);
        this.literal = builder.literal;
    }

    @Override
    public String toString() {
        return LogUtils.withArgs("""
                LiteralCommandNode{
                    literal: {},
                    level: {}
                }""", literal, level);
    }
}
