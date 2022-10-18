package me.cometkaizo.wavetech.parser.syntaxparseres;

import me.cometkaizo.wavetech.lexer.tokens.Token;

import java.util.function.Function;

public class TransformationMatcher {

    private final Function<Token, Token> transformation;
    private final Object expected;
    private final boolean includesNone;

    public TransformationMatcher(Function<Token, Token> transformation, Object expected) {
        this.transformation = transformation;
        this.expected = expected;
        if (expected instanceof AnyMatcher anyMatcher) {
            this.includesNone = anyMatcher.includesNone();
        } else this.includesNone = false;
    }
    public TransformationMatcher(Function<Token, Token> transformation) {
        this.transformation = transformation;
        this.expected = null;
        this.includesNone = false;
    }

    public Token transform(Token token) {
        return transformation.apply(token);
    }

    public boolean matches(Token candidate) {
        if (expected == null) return true;

        Token transformedCandidate = transform(candidate);

        if (expected instanceof AnyMatcher anyMatcher) {
            //LogUtils.info("expected value is anyMatcher");
            return anyMatcher.matches(transformedCandidate.getType());
        } else if (expected instanceof TransformationMatcher transformationMatcher) {
            //LogUtils.info("expected value is transformationMatcher");
            return transformationMatcher.matches(transformedCandidate) ||
                    transformationMatcher.includesNone;
        } else if (expected instanceof PredicateAnyMatcher predicateAnyMatcher) {
            //LogUtils.info("expected value is predicate matcher");
            //LogUtils.warn("actual value does not match predicate matcher");
            return predicateAnyMatcher.matches(transformedCandidate);
        } else {
            //LogUtils.warn("expected value {} does not equal actual value {}", expectedToken, currentToken;
            return expected.equals(transformedCandidate.getType());
        }
    }

    public boolean includesNone() {
        return includesNone;
    }
}
