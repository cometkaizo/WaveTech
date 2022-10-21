package me.cometkaizo.wavetech.parser.syntaxes.nodes;

import me.cometkaizo.util.LogUtils;
import me.cometkaizo.wavetech.lexer.tokens.Token;

import java.util.function.Function;

class ConditionalSyntaxNode extends SyntaxNode {

    protected final Function<Token, Boolean> condition;

    public ConditionalSyntaxNode(ConditionalSyntaxNodeBuilder builder) {
        super(builder);
        this.condition = builder.condition;
    }

    @Override
    protected Token transform(Token nextToken) {
        return nextToken;
    }

    @Override
    protected boolean accepts(Token nextToken) {
        return condition.apply(nextToken);
    }

    @Override
    protected String representData() {
        return "CONDITIONAL";
    }

    @Override
    public String toString() {
        return LogUtils.withArgs("""
                ConditionalCommandNode{
                    condition: {},
                }""", condition);
    }
}
