package me.cometkaizo.wavetech.parser.syntaxes.nodes;

import me.cometkaizo.util.LogUtils;
import me.cometkaizo.wavetech.lexer.tokens.Token;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public class LiteralSyntaxNodeBuilder extends SyntaxNodeBuilder {

    @NotNull
    protected final Token expected;
    @NotNull
    protected final Function<Token, Token> transformation;

    public LiteralSyntaxNodeBuilder(@NotNull Token expected) {
        this.expected = expected;
        this.transformation = token -> token;
    }
    public LiteralSyntaxNodeBuilder(@NotNull Token expected, @NotNull Function<Token, Token> transformation) {
        this.expected = expected;
        this.transformation = transformation;
    }

    @Override
    protected TokenSyntaxNode build() {
        return new TokenSyntaxNode(this);
    }

    @Override
    public String toString() {
        return LogUtils.withArgs("""
                LiteralCommandNodeBuilder{
                    expected: {}
                }""", expected);
    }
}
