package me.cometkaizo.wavetech.parser.syntaxes.nodes;

import me.cometkaizo.util.LogUtils;
import me.cometkaizo.wavetech.lexer.tokens.Token;
import me.cometkaizo.wavetech.parser.Parser;
import me.cometkaizo.wavetech.parser.nodes.DeclaredNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public abstract class MatcherFunction {
    private Token lastToken = null;
    private Token lastPeekedToken = null;
    private DeclaredNode lastContext = null;
    private Parser.Status lastStatus = null;
    protected final String name;

    protected MatcherFunction() {
        this("anonymous");
    }

    protected MatcherFunction(String name) {
        this.name = name;
    }

    @NotNull
    public final Syntax.Result match(Token token, Token nextToken, DeclaredNode context, Parser.Status status) {
        lastToken = token;
        lastPeekedToken = nextToken;
        lastContext = context;
        lastStatus = status;
        return match(token, nextToken);
    }

    @NotNull
    protected abstract Syntax.Result match(@NotNull Token token, @Nullable Token nextToken);


    protected final Map<String, Syntax> syntaxes = new HashMap<>();

    protected Syntax.Result matchNextFor(Supplier<? extends Syntax> syntax, String id) {
        LogUtils.debug("syntaxes: {}", syntaxes);
        if (!syntaxes.containsKey(id)) {
            Syntax builtSyntax = syntax.get();
            LogUtils.debug("put id '{}' syntax {} into {}", id, builtSyntax, syntaxes);
            syntaxes.put(id, builtSyntax);
        }
        Syntax.Result result = syntaxes.get(id).matchNext(lastToken, lastPeekedToken, lastContext, lastStatus);
        LogUtils.debug("result for {}: {} with tokens {}, peeking {}", syntaxes.get(id), result, lastToken, lastPeekedToken);
        return result;
    }

    protected Syntax.Result matchNextFor(Syntax syntax) {
        Syntax.Result result = syntax.matchNext(lastToken, lastPeekedToken, lastContext, lastStatus);
        LogUtils.debug("syntax {} matches? {} to {}, peeking {}",
                syntax.getClass().getSimpleName(), result, lastToken, lastPeekedToken);
        return result;
    }

}
