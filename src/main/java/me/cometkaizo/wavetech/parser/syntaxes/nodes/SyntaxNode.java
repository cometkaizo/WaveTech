package me.cometkaizo.wavetech.parser.syntaxes.nodes;

import me.cometkaizo.util.LogUtils;
import me.cometkaizo.wavetech.lexer.tokens.Token;
import me.cometkaizo.wavetech.parser.Parser;
import me.cometkaizo.wavetech.parser.nodes.DeclaredNode;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * A SyntaxNode represents a single expected token in a syntax (e.g. keywords, symbols, modifiers, etc).
 * Specific functionality is specified by subclasses.
 *
 * @see Syntax
 * @see RootSyntaxNode
 * @see TokenSyntaxNode
 * @see TokenTypeSyntaxNode
 * @see SoftSyntaxNode
 * @see ConditionalSyntaxNode
 */
public abstract class SyntaxNode {

    final Syntax syntax;
    protected final List<SyntaxNode> subNodes;
    protected final List<Runnable> tasks;
    protected final List<MatcherFunction> peekers;
    protected final List<Function<Token, Boolean>> failConditions;


    private final boolean hasTokenAcceptingSubNodes;
    private final boolean acceptsToken;
    final boolean splits;
    final boolean merges;
    private final int depth;
    private DeclaredNode context = null;
    private Parser.Status status = null;

    protected SyntaxNode(SyntaxNodeBuilder builder) {
        this.subNodes = buildSubNodes(builder);
        this.tasks = List.of(builder.tasks.toArray(Runnable[]::new));
        this.peekers = List.of(builder.peekers.toArray(MatcherFunction[]::new));
        this.failConditions = List.copyOf(new ArrayList<>(builder.failConditions));


        this.syntax = builder.syntax;
        this.hasTokenAcceptingSubNodes = builder.hasTokenAcceptingSubNodes();
        this.acceptsToken = builder.acceptsToken;
        this.splits = builder.splits;
        this.merges = builder.merges;
        this.depth = builder.calculateDepth();
    }

    private List<SyntaxNode> buildSubNodes(SyntaxNodeBuilder builder) {
        LinkedHashSet<SyntaxNode> result = builder.subNodes.stream()
                .map(SyntaxNodeBuilder::build)
                .collect(Collectors.toCollection(LinkedHashSet::new));
        return List.of(result.toArray(SyntaxNode[]::new));
    }


    public record MatchResult(Syntax.Result result, SyntaxNode nextNode, SyntaxNode subNode) {

    }


    // TODO: 2022-11-07 switch to matches() for the current node, not getting the next sub-node that matches
    protected final MatchResult getNextNodeMatching(Token token, Token nextToken, DeclaredNode context, Parser.Status status) {
        this.context = context;
        this.status = status;

        if (!hasSubNodes()) {
            LogUtils.error("this: {}", this);
            return new MatchResult(Syntax.Result.NO_MATCH, null, null);
        }

        if (!syntax.inputTokens.containsKey(token.getType().getClass()))
            syntax.inputTokens.put(token.getType().getClass(), new ArrayList<>());
        List<Token> inputTokenList = syntax.inputTokens.get(token.getType().getClass());

        MatchResult matchResult = getValidNextNode(token, nextToken, inputTokenList);

        if (matchResult.result == Syntax.Result.MATCHES_SO_FAR) {
            for (MatcherFunction peeker : peekers) {
                Syntax.Result peekResult = peeker.match(token, nextToken, context, status);
                if (peekResult == Syntax.Result.NO_MATCH)
                    return new MatchResult(Syntax.Result.NO_MATCH, null, null);
                if (peekResult == Syntax.Result.MATCHES_EXACT)
                    return new MatchResult(Syntax.Result.MATCHES_EXACT, matchResult.subNode, matchResult.subNode);
            }
        }

        return matchResult;
    }

    @NotNull
    private MatchResult getValidNextNode(Token token, Token nextToken, List<Token> inputTokenList) {
        for (SyntaxNode subNode : subNodes) {
            if (!subNode.accepts(context, status)) continue;

            for (var failCondition : subNode.failConditions) {
                if (failCondition.apply(nextToken)) {
                    LogUtils.debug("fail condition in {} applied to {}", getClass().getSimpleName(), nextToken);
                    return new MatchResult(Syntax.Result.NO_MATCH, null, null);
                }
                LogUtils.debug("fail condition in {} apply to {}", getClass().getSimpleName(), nextToken);
            }

            if (subNode instanceof LoopingSyntaxNode looper) {

                Syntax.Result checkResult = looper.checkNext(token, nextToken, context, status);
                LogUtils.info("check result for looper with tokens {}, {}: {}", token, nextToken, checkResult);

                if (checkResult == Syntax.Result.NO_MATCH) {
                    return new MatchResult(Syntax.Result.NO_MATCH, null, null);
                }

                inputTokenList.add(token);
                if (checkResult == Syntax.Result.MATCHES_SO_FAR) {
                    LogUtils.debug("loop {} matches so far, returning this {}", subNode.representData(), representData());
                    return new MatchResult(Syntax.Result.MATCHES_SO_FAR, this, subNode);
                }
                if (checkResult == Syntax.Result.MATCHES_EXACT) {
                    if (subNode.hasTokenAcceptingSubNodes)
                        return new MatchResult(Syntax.Result.MATCHES_SO_FAR, subNode, subNode);
                    return new MatchResult(Syntax.Result.MATCHES_EXACT, subNode, subNode);
                }
            }

            if (!subNode.acceptsToken()) {
                subNode.runTasks();
                return subNode.getNextNodeMatching(token, nextToken, context, status);
            }

            if (subNode.accepts(token)) {
                inputTokenList.add(subNode.transform(token));

                if (subNode.hasTokenAcceptingSubNodes)
                    return new MatchResult(Syntax.Result.MATCHES_SO_FAR, subNode, subNode);
                return new MatchResult(Syntax.Result.MATCHES_EXACT, subNode, subNode);
            }

        }
        if (hasTokenAcceptingSubNodes)
            return new MatchResult(Syntax.Result.NO_MATCH, null, null);
        throw new AssertionError();
    }

    public void runTasks() {
        for (Runnable task : tasks) {
            task.run();
        }
    }
    public void runAllTasks() {
        runTasks();
        for (SyntaxNode subNode : subNodes) {
            subNode.runAllTasks();
        }
    }

    protected boolean accepts(DeclaredNode context, Parser.Status status) {
        return true;
    }

    protected abstract Token transform(Token nextToken);

    public boolean acceptsToken() {
        return acceptsToken;
    }



    protected abstract boolean accepts(Token candidate);

    public boolean hasSubNodes() {
        return subNodes.size() > 0;
    }

    public abstract boolean equals(Object o);

    public abstract int hashCode();

    public String deepToString() {
        return SyntaxNode.class.getSimpleName() + "$" + getClass().getSimpleName() + "{subNodes:" +
                (subNodes.size() > 0 ? subNodes.stream()
                        .map(syntaxNode -> syntaxNode.deepToString().indent(2))
                        .collect(Collectors.joining("\n", "\n", "")) : "none") +
                "}";
    }

    public boolean hasTokenAcceptingSubNodes() {
        return hasTokenAcceptingSubNodes;
    }

    protected abstract String representData();

    public int getDepth() {
        return depth;
    }

}
