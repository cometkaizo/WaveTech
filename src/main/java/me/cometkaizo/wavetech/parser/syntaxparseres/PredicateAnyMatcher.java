package me.cometkaizo.wavetech.parser.syntaxparseres;

import me.cometkaizo.wavetech.lexer.tokens.Token;

import java.util.function.Predicate;

public class PredicateAnyMatcher {

    private final Predicate<Token>[] conditions;

    @SafeVarargs
    public PredicateAnyMatcher(Predicate<Token>... conditions) {
        this.conditions = conditions;
    }

    public boolean matches(Object candidate) {
        if (conditions.length == 0) return false;
        if (candidate instanceof Token token) {
            for (Predicate<Token> condition : conditions) {
                if (!condition.test(token)) return false;
            }
            return true;
        } else {
            return false;
        }
    }

}
