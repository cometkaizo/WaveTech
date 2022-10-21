package me.cometkaizo.wavetech.parser.syntaxes.nodes;

import me.cometkaizo.wavetech.lexer.tokens.Token;
import me.cometkaizo.wavetech.lexer.tokens.TokenType;
import org.jetbrains.annotations.NotNull;

import java.util.stream.Collectors;

public class TokenTypeSyntaxNode extends SyntaxNode {

    @NotNull
    protected final TokenType type;
    @NotNull
    protected final TokenType transformedType;

    public TokenTypeSyntaxNode(TokenTypeSyntaxNodeBuilder builder) {
        super(builder);
        this.type = builder.type;
        this.transformedType = builder.transformedType;
    }

    @Override
    protected Token transform(@NotNull Token nextToken) {
        if (transformedType == type) return nextToken;
        return nextToken.getValue() == null? new Token(transformedType) : new Token(transformedType, nextToken.getValue());
    }

    @Override
    protected boolean accepts(@NotNull Token candidate) {
        return candidate.getType() == type;
    }

    @Override
    public String deepToString() {
        return getClass().getSimpleName() + "{" +
                "type=" + type +
                (type == transformedType? "" : ",transformed=" + transformedType) +
                ",subNodes:" +
                        (subNodes.size() > 0 ? subNodes.stream()
                                .map(syntaxNode -> syntaxNode.deepToString().indent(2))
                                .collect(Collectors.joining("\n", "\n", "")) : "none") +
                "}";
    }

    @Override
    protected String representData() {
        return (type == transformedType? "" : transformedType + " from ") + type;
    }

    @Override
    public String toString() {
        return "TokenTypeSyntaxNode{" +
               "type=" + type +
               ", transformedType=" + transformedType +
               '}';
    }
}
