package me.cometkaizo.commands.nodes;

import me.cometkaizo.commands.arguments.Argument;
import me.cometkaizo.logging.LogUtils;

public class ArgumentCommandNodeBuilder extends CommandNodeBuilder {

    protected final Argument argument;

    public ArgumentCommandNodeBuilder(Argument argument) {
        this.argument = argument;
    }

    @Override
    protected ArgumentCommandNode build() {
        return new ArgumentCommandNode(this);
    }

    @Override
    public String toString() {
        return LogUtils.withArgs("""
                ArgumentCommandNodeBuilder{
                    argument: {},
                    level: {}
                }""", argument, level);
    }
}
