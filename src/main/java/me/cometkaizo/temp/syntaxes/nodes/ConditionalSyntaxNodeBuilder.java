package me.cometkaizo.temp.syntaxes.nodes;

import me.cometkaizo.util.ClassUtils;
import me.cometkaizo.wavetech.lexer.tokens.Token;
import me.cometkaizo.wavetech.lexer.tokens.TokenType;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public class ConditionalSyntaxNodeBuilder extends SyntaxNodeBuilder {

    @NotNull
    protected final Function<Token, Boolean> condition;

    public ConditionalSyntaxNodeBuilder(@NotNull Function<Token, Boolean> condition) {
        this.condition = condition;
    }
    public ConditionalSyntaxNodeBuilder(@NotNull Class<? extends TokenType> typeRequirement) {
        this(token -> ClassUtils.isInstanceOf(token.getType(), typeRequirement));
    }

    @Override
    protected ConditionalSyntaxNode build() {
        return new ConditionalSyntaxNode(this);
    }
}
