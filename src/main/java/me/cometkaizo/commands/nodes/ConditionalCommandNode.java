package me.cometkaizo.commands.nodes;

import me.cometkaizo.util.LogUtils;

import java.util.function.Supplier;

class ConditionalCommandNode extends SoftCommandNode {

    protected final Supplier<Boolean> condition;

    public ConditionalCommandNode(ConditionalCommandNodeBuilder builder) {
        super(builder);
        this.condition = builder.condition;
    }

    @Override
    protected boolean accepts() {
        return condition.get();
    }

    @Override
    public String toString() {
        return LogUtils.withArgs("""
                ConditionalCommandNode{
                    condition: {},
                    level: {}
                }""", condition, level);
    }
}
