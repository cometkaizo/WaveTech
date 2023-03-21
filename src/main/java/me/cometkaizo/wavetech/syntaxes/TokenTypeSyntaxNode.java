package me.cometkaizo.wavetech.syntaxes;

import me.cometkaizo.logging.LogUtils;
import me.cometkaizo.util.PeekingIterator;
import me.cometkaizo.wavetech.lexer.tokens.Token;
import me.cometkaizo.wavetech.lexer.tokens.TokenType;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class TokenTypeSyntaxNode extends ResultBearingSyntaxNode<Token> {

    @NotNull
    protected final TokenType type;

    public TokenTypeSyntaxNode(TokenTypeSyntaxNodeBuilder builder) {
        super(builder);
        this.type = builder.type;
    }

    @Override
    protected boolean matchAndStore(PeekingIterator<Token> tokens, SyntaxStructure structure, SyntaxMatcher matcher) {
        if (!tokens.hasNext()) return false;
        LogUtils.info("token type: {}", type);
        Token token = tokens.next();

        boolean isSameType = type.equals(token.getType());

        LogUtils.info("cursor: {}, current: {}, required: {}, isSameType? {}", tokens.cursor(), token, type, isSameType);

        if (isSameType) store(token, structure);
        return isSameType;
    }

    @Override
    public String toPrettyString() {
        return String.valueOf(type);
    }

    @Override
    public String toString() {
        return "TokenTypeSyntaxNode{" +
               "type=" + type +
               '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TokenTypeSyntaxNode that = (TokenTypeSyntaxNode) o;
        return type.equals(that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type);
    }
}
