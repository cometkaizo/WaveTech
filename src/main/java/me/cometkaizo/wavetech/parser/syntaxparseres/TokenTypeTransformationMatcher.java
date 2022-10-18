package me.cometkaizo.wavetech.parser.syntaxparseres;

import me.cometkaizo.wavetech.lexer.tokens.Token;
import me.cometkaizo.wavetech.lexer.tokens.TokenType;

public class TokenTypeTransformationMatcher extends TransformationMatcher {
    private final TokenType originalTokenType;

    public TokenTypeTransformationMatcher(TokenType originalTokenType, TokenType to, Object expected) {
        super(token -> {
            if (token.getType() == originalTokenType) {
                if (token.getValue() == null) {
                    return new Token(to);
                }
                return new Token(to, token.getValue());
            }
            return token;
        }, expected);
        this.originalTokenType = originalTokenType;
    }

    public TokenTypeTransformationMatcher(TokenType originalTokenType, TokenType to) {
        super(token -> {
            if (token.getType() == originalTokenType) {
                if (token.getValue() == null) {
                    return new Token(to);
                }
                return new Token(to, token.getValue());
            }
            return token;
        });
        this.originalTokenType = originalTokenType;
    }

    @Override
    public boolean matches(Token candidate) {
        return candidate.getType() == originalTokenType && super.matches(candidate);
    }
}
