package me.cometkaizo.wavetech.parser.syntaxes.nodes;

import me.cometkaizo.util.LogUtils;
import me.cometkaizo.wavetech.lexer.tokens.Token;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.Function;

class TokenSyntaxNode extends SyntaxNode {

    @NotNull
    private final Token expected;
    @NotNull
    private final Function<Token, Token> transformation;


    @Override
    protected boolean accepts(Token candidate) {
        return expected.equals(candidate);
    }


    @Override
    protected String representData() {
        return expected.toString();
    }

    @Override
    protected Token transform(Token nextToken) {
        return transformation.apply(nextToken);
    }

    public TokenSyntaxNode(LiteralSyntaxNodeBuilder builder) {
        super(builder);
        this.expected = builder.expected;
        this.transformation = builder.transformation;
    }

    @Override
    public String toString() {
        return LogUtils.withArgs("""
                LiteralCommandNode{
                    expected: {}
                }""", expected);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TokenSyntaxNode that = (TokenSyntaxNode) o;
        return expected.equals(that.expected) && transformation.equals(that.transformation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(expected, transformation);
    }
}
