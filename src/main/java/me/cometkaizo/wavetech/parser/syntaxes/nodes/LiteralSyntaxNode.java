package me.cometkaizo.wavetech.parser.syntaxes.nodes;

import me.cometkaizo.util.LogUtils;
import me.cometkaizo.wavetech.lexer.tokens.Token;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

class LiteralSyntaxNode extends SyntaxNode {

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

    public LiteralSyntaxNode(LiteralSyntaxNodeBuilder builder) {
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
}
