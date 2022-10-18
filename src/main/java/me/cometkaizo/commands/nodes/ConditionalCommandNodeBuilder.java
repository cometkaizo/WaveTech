package me.cometkaizo.commands.nodes;

import java.util.function.Supplier;

public class ConditionalCommandNodeBuilder extends SoftCommandNodeBuilder {

    protected final Supplier<Boolean> condition;

    public ConditionalCommandNodeBuilder(Supplier<Boolean> condition) {
        this.condition = condition;
    }

    @Override
    protected ConditionalCommandNode build() {
        return new ConditionalCommandNode(this);
    }
}
