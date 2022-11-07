package me.cometkaizo.temp.syntaxes.nodes;

import me.cometkaizo.temp.Parser;
import me.cometkaizo.temp.nodes.DeclaredNode;
import me.cometkaizo.util.StringUtils;

import java.util.function.Function;

class SyntaxSyntaxNode extends SoftSyntaxNode {
    protected final Function<DeclaredNode, Boolean> contextCheck;
    protected final Function<Parser.Status, Boolean> statusCheck;
    private final Class<? extends Syntax> syntaxType;

    protected SyntaxSyntaxNode(SyntaxSyntaxNodeBuilder builder) {
        super(builder);
        this.contextCheck = builder.contextCheck;
        this.statusCheck = builder.statusCheck;
        this.syntaxType = builder.syntaxType;
    }

    @Override
    protected boolean accepts() {
        throw new AssertionError();
    }

    @Override
    protected boolean accepts(DeclaredNode context, Parser.Status status) {
        return contextCheck.apply(context) && statusCheck.apply(status);
    }

    @Override
    protected String representData() {
        return '(' + StringUtils.splitLast(syntaxType.getName(), "\\.") + ')';
    }
}
