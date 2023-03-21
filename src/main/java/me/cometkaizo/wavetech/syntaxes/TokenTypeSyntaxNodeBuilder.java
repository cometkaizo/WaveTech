package me.cometkaizo.wavetech.syntaxes;

import me.cometkaizo.wavetech.lexer.tokens.Token;
import me.cometkaizo.wavetech.lexer.tokens.TokenType;
import org.jetbrains.annotations.NotNull;

public class TokenTypeSyntaxNodeBuilder extends ResultBearingSyntaxNodeBuilder<Token> {

    @NotNull
    protected final TokenType type;

    public TokenTypeSyntaxNodeBuilder(@NotNull TokenType type) {
        this.type = type;
    }

    @Override
    protected TokenTypeSyntaxNode build() {
        return new TokenTypeSyntaxNode(this);
    }

    @Override
    public String toString() {
        return "TokenTypeSyntaxNodeBuilder{" +
               "type=" + type +
               '}';
    }

}
