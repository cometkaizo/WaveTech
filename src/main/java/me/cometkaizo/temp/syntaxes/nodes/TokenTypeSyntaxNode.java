package me.cometkaizo.temp.syntaxes.nodes;

import me.cometkaizo.wavetech.lexer.tokens.Token;
import me.cometkaizo.wavetech.lexer.tokens.TokenType;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
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
        return String.valueOf(transformedType);
    }

    @Override
    public String toString() {
        return "TokenTypeSyntaxNode{" +
               "type=" + type +
               ", transformedType=" + transformedType +
               '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TokenTypeSyntaxNode that = (TokenTypeSyntaxNode) o;
        return type.equals(that.type) && transformedType.equals(that.transformedType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, transformedType);
    }
}
