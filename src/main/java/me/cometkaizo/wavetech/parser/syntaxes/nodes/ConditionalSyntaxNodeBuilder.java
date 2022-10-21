package me.cometkaizo.wavetech.parser.syntaxes.nodes;

import me.cometkaizo.wavetech.lexer.tokens.Token;

import java.util.function.Function;

public class ConditionalSyntaxNodeBuilder extends SyntaxNodeBuilder {

    protected final Function<Token, Boolean> condition;

    public ConditionalSyntaxNodeBuilder(Function<Token, Boolean> condition) {
        this.condition = condition;
    }

    @Override
    protected ConditionalSyntaxNode build() {
        return new ConditionalSyntaxNode(this);
    }
}
