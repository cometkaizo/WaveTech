package me.cometkaizo.temp.syntaxes.nodes;

import me.cometkaizo.temp.Parser;
import me.cometkaizo.temp.nodes.DeclaredNode;
import me.cometkaizo.util.LogUtils;

import java.util.function.Function;

class SyntaxSyntaxNodeBuilder extends SoftSyntaxNodeBuilder {
    protected final Function<DeclaredNode, Boolean> contextCheck;
    protected final Function<Parser.Status, Boolean> statusCheck;
    public final Class<? extends Syntax> syntaxType;

    public SyntaxSyntaxNodeBuilder(Syntax syntax) {
        this.contextCheck = syntax::isValidInContext;
        this.statusCheck = syntax::isValidInStatus;
        this.syntaxType = syntax.getClass();
        this.focus = syntax.rootNode.focus;
        this.splits = syntax.rootNode.splits;
        this.merges = syntax.rootNode.merges;

        this.tasks.addAll(syntax.rootNode.tasks);
        this.subNodes.addAll(syntax.rootNode.subNodes);
        LogUtils.debug("representingSyntax: {}, subNodes: {}", syntax.getClass().getName(), subNodes);
    }

    @Override
    protected SyntaxNode build() {
        return new SyntaxSyntaxNode(this);
    }

    @Override
    public String toString() {
        return "SyntaxSyntaxNodeBuilder{" +
                "syntaxType=" + syntaxType.getName() +
                '}';
    }
}
