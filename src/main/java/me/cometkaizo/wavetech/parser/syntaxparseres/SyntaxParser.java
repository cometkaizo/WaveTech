package me.cometkaizo.wavetech.parser.syntaxparseres;

import me.cometkaizo.util.LogUtils;
import me.cometkaizo.wavetech.lexer.tokens.Token;
import me.cometkaizo.wavetech.lexer.tokens.TokenType;
import me.cometkaizo.wavetech.parser.Parser;
import me.cometkaizo.wavetech.parser.nodes.DeclaredNode;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * Defines syntaxes with expected patterns and valid scopes. Subclasses must be registered in {@link SyntaxParsers}.
 */
public abstract class SyntaxParser {

    private final List<Object[]> expectedPatterns = new ArrayList<>();
    private final List<Integer> expectedValueIndexes = new ArrayList<>();
    private Token currentToken = null;
    private List<Object[]> validPatterns = new ArrayList<>();

    private final List<Map<Class<? extends TokenType>, List<Token>>> inputTokens = new ArrayList<>();
    private int exactMatchingInputPatternIndex = -1;

    private final List<SyntaxParser> nextExpectedPatterns = new ArrayList<>();


    protected final void addExpectedSyntax(Object... expectedPattern) {
        expectedPatterns.add(expectedPattern);
        validPatterns.add(expectedPattern);
        expectedValueIndexes.add(0);
        inputTokens.add(new HashMap<>());
    }
    protected final void addNextExpectedSyntax(SyntaxParser nextExpectedPattern) {
        this.nextExpectedPatterns.add(nextExpectedPattern);
    }

    protected abstract boolean isValidInStatus(Parser.ParserStatus status);
    protected abstract boolean isValidInContext(DeclaredNode context);


    public Result matchNext(@NotNull DeclaredNode context, @NotNull Parser.ParserStatus status, @NotNull Token nextToken) {
        if (!isValidInContext(context)) {
            return Result.NO_RESULT;
        } if (!isValidInStatus(status)) {
            return Result.NO_RESULT;
        }
        currentToken = nextToken;

        LogUtils.warn("syntax parser type: {}, next token: {}", getClass().getName(), nextToken);

        Result bestResult = Result.NO_RESULT;
        List<Object[]> newValidPatterns = new ArrayList<>();

        for (int patternIndex = 0; patternIndex < expectedPatterns.size(); patternIndex ++) {
            Result matchResult = parseCurrentToken(patternIndex);

            if (matchResult == Result.MATCHES_EXACT || matchResult == Result.MATCHES_SO_FAR) {

                newValidPatterns.add(expectedPatterns.get(patternIndex));
                if (matchResult == Result.MATCHES_EXACT) {
                    exactMatchingInputPatternIndex = patternIndex;
                    return matchResult;
                } else {
                    bestResult = matchResult;

                }
            }

        }

        validPatterns = newValidPatterns;

        return bestResult;
    }

    private Result parseCurrentToken(int patternIndex) {
        Object[] expectedPattern = expectedPatterns.get(patternIndex);

        if (expectedValueIndexes.get(patternIndex) == expectedPattern.length) {
            LogUtils.warn("expected value index {} is greater than expected pattern length {}", expectedValueIndexes.get(patternIndex), expectedPattern.length);
            return Result.NO_RESULT;
        }

        // check if current value matches to current expected value.
        // If not, and it is optional, advance to next expected value

        while (hasNextExpectedValue(patternIndex)) {

            var expectedToken = expectedPattern[expectedValueIndexes.get(patternIndex)];

            expectedValueIndexes.set(patternIndex, expectedValueIndexes.get(patternIndex) + 1);

            if (expectedToken instanceof TransformationMatcher transformationMatcher) {

                Map<Class<? extends TokenType>, List<Token>> inputTokenMap = inputTokens.get(patternIndex);
                if (!inputTokenMap.containsKey(currentToken.getType().getClass()))
                    inputTokenMap.put(currentToken.getType().getClass(), new ArrayList<>());

                if (transformationMatcher.includesNone()) {
                    LogUtils.info("expected value is Optional transformationMatcher");
                    if (transformationMatcher.matches(currentToken)) {
                        inputTokenMap.get(currentToken.getType().getClass()).add(transformationMatcher.transform(currentToken));
                        LogUtils.info("{} Optional transformationMatcher matches {}", getClass(), currentToken);
                        return !hasNextExpectedValue(patternIndex) ? Result.MATCHES_EXACT : Result.MATCHES_SO_FAR;
                    }
                    LogUtils.info("{} Optional transformationMatcher did not match {}", getClass(), currentToken);
                } else {
                    inputTokenMap.get(currentToken.getType().getClass()).add(transformationMatcher.transform(currentToken));
                    LogUtils.info("expected value is Required transformationMatcher");
                    if (transformationMatcher.matches(currentToken)) {
                        LogUtils.info("{} Required transformationMatcher matches {}", getClass(), currentToken);
                        return !hasNextExpectedValue(patternIndex) ? Result.MATCHES_EXACT : Result.MATCHES_SO_FAR;
                    } else {
                        LogUtils.info("{} Required transformationMatcher did not match {}", getClass(), currentToken);
                        return Result.NO_RESULT;
                    }
                }
            } else {

                Map<Class<? extends TokenType>, List<Token>> inputTokenMap = inputTokens.get(patternIndex);
                if (!inputTokenMap.containsKey(currentToken.getType().getClass()))
                    inputTokenMap.put(currentToken.getType().getClass(), new ArrayList<>());

                if (expectedToken instanceof AnyMatcher anyMatcher) {
                    if (anyMatcher.includesNone()) {
                        LogUtils.info("expected value is Optional anyMatcher");
                        if (anyMatcher.matches(currentToken.getType())) {
                            LogUtils.info("{} Optional anyMatcher matches value {}", getClass(), currentToken);

                            inputTokenMap.get(currentToken.getType().getClass()).add(currentToken);
                            return !hasNextExpectedValue(patternIndex) ? Result.MATCHES_EXACT : Result.MATCHES_SO_FAR;
                        } else {

                            LogUtils.info("{} Optional anyMatcher did not match value {}", getClass(), currentToken);
                        }
                    } else {
                        LogUtils.info("expected value is Required anyMatcher");
                        inputTokenMap.get(currentToken.getType().getClass()).add(currentToken);
                        if (anyMatcher.matches(currentToken.getType())) {
                            LogUtils.info("required anyMatcher matches {}", currentToken);
                            return !hasNextExpectedValue(patternIndex) ? Result.MATCHES_EXACT : Result.MATCHES_SO_FAR;
                        } else {
                            LogUtils.info("required anyMatcher did not match {}", currentToken);
                            return Result.NO_RESULT;
                        }
                    }
                } else if (expectedToken instanceof PredicateAnyMatcher predicateAnyMatcher) {
                    LogUtils.info("expected value is predicate matcher");
                    //LogUtils.warn("actual value does not match predicate matcher");

                    inputTokenMap.get(currentToken.getType().getClass()).add(currentToken);
                    if (predicateAnyMatcher.matches(currentToken)) {
                        LogUtils.info("predicate matcher matches {}", currentToken);
                        return !hasNextExpectedValue(patternIndex) ? Result.MATCHES_EXACT : Result.MATCHES_SO_FAR;
                    } else {
                        LogUtils.info("predicate matcher does not match {}", currentToken);
                        return Result.NO_RESULT;
                    }
                } else {

                    inputTokenMap.get(currentToken.getType().getClass()).add(currentToken);
                    //LogUtils.warn("expected value {} does not equal actual value {}", expectedToken, currentToken;
                    if (expectedToken.equals(currentToken.getType())) {
                        LogUtils.info("expected object {} equals actual token type {}", expectedToken, currentToken.getType());
                        return !hasNextExpectedValue(patternIndex) ? Result.MATCHES_EXACT : Result.MATCHES_SO_FAR;
                    } else {
                        LogUtils.info("expected object {} does not equal actual token type {}", expectedToken, currentToken.getType());
                        return Result.NO_RESULT;
                    }
                }
            }

        }

        LogUtils.warn("returned MATCHES_SO_FAR because of end of loop");
        return Result.MATCHES_SO_FAR;
    }

    private boolean hasNextExpectedValue(int patternIndex) {
        return expectedValueIndexes.get(patternIndex) < expectedPatterns.get(patternIndex).length;
    }

    public List<Object[]> getExpectedPatterns() {
        return expectedPatterns;
    }

    public List<SyntaxParser> getNextExpectedPatterns() {
        return nextExpectedPatterns;
    }

    /**
     * Gets the list of input tokens that matched this syntax parser exactly
     */
    public Map<Class<? extends TokenType>, List<Token>> getExactMatchingInputPattern() {
        return inputTokens.get(exactMatchingInputPatternIndex);
    }

    public int getExactMatchingInputPatternIndex() {
        return exactMatchingInputPatternIndex;
    }

    public enum Result {

        NO_RESULT,
        MATCHES_SO_FAR,
        MATCHES_EXACT

    }
}
