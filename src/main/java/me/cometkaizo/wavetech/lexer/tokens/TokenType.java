package me.cometkaizo.wavetech.lexer.tokens;

import me.cometkaizo.wavetech.syntaxes.TokenTypeSyntaxNodeBuilder;
import org.jetbrains.annotations.Nullable;

public interface TokenType {

    @Nullable
    String symbol();

    default TokenTypeSyntaxNodeBuilder node() {
        return new TokenTypeSyntaxNodeBuilder(this);
    }

}
