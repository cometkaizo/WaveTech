package me.cometkaizo.wavetech.syntaxes;

import me.cometkaizo.util.ClassUtils;
import me.cometkaizo.wavetech.lexer.tokens.Token;
import me.cometkaizo.wavetech.lexer.tokens.TokenType;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

public class ConditionalSyntaxNodeBuilder extends ResultBearingSyntaxNodeBuilder<Token> {

    @NotNull
    protected final Predicate<Token> condition;

    public ConditionalSyntaxNodeBuilder(@NotNull Predicate<Token> condition) {
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
