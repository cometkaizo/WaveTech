package me.cometkaizo.commands.nodes;

import me.cometkaizo.logging.LogUtils;

public class LiteralCommandNodeBuilder extends CommandNodeBuilder {

    protected final String literal;

    public LiteralCommandNodeBuilder(String literal) {
        this.literal = literal;
    }

    @Override
    protected LiteralCommandNode build() {
        return new LiteralCommandNode(this);
    }

    @Override
    public String toString() {
        return LogUtils.withArgs("""
                LiteralCommandNodeBuilder{
                    literal: {}
                }""", literal);
    }
}
