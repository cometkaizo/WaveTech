package me.cometkaizo.temp.syntaxes.nodes;

import me.cometkaizo.wavetech.lexer.tokens.TokenType;
import org.jetbrains.annotations.NotNull;

public class TokenTypeSyntaxNodeBuilder extends SyntaxNodeBuilder {

    @NotNull
    protected final TokenType type;
    @NotNull
    protected final TokenType transformedType;

    public TokenTypeSyntaxNodeBuilder(@NotNull TokenType type) {
        this.type = type;
        this.transformedType = type;
    }
    public TokenTypeSyntaxNodeBuilder(@NotNull TokenType type, @NotNull TokenType transformedType) {
        this.type = type;
        this.transformedType = transformedType;
    }

    @Override
    protected SyntaxNode build() {
        return new TokenTypeSyntaxNode(this);
    }

    @Override
    public String toString() {
        return "TokenTypeSyntaxNodeBuilder{" +
               "type=" + type +
               ", transformedType=" + transformedType +
               '}';
    }
}
