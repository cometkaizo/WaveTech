package me.cometkaizo.temp.syntaxes.nodes;

import org.jetbrains.annotations.NotNull;

class LoopingSyntaxNodeBuilder extends SyntaxNodeBuilder {

    @NotNull
    protected final MatcherFunction function;

    public LoopingSyntaxNodeBuilder(@NotNull MatcherFunction function) {
        this.function = function;
    }

    @Override
    protected SyntaxNode build() {
        return new LoopingSyntaxNode(this);
    }
}
