package me.cometkaizo.commands.nodes;

class EmptyCommandNodeBuilder extends SoftCommandNodeBuilder {

    @Override
    protected EmptyCommandNode build() {
        return new EmptyCommandNode(this);
    }
}
