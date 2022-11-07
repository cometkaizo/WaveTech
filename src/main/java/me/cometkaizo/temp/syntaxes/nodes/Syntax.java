package me.cometkaizo.temp.syntaxes.nodes;

import me.cometkaizo.annotations.Legacy;
import me.cometkaizo.temp.Parser;
import me.cometkaizo.temp.nodes.DeclaredNode;
import me.cometkaizo.temp.nodes.Node;
import me.cometkaizo.temp.syntaxes.visitors.EmptyParserVisitor;
import me.cometkaizo.temp.syntaxes.visitors.ParserStatusVisitor;
import me.cometkaizo.util.LogUtils;
import me.cometkaizo.wavetech.lexer.tokens.Token;
import me.cometkaizo.wavetech.lexer.tokens.TokenType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The Syntax class represents a way to parse a series of tokens into {@link Node}s.
 * Subclasses define what types of tokens are to be expected, and what to do with them.
 * The {@link RootSyntaxNode} {@code root} field is the root of a node tree which specifies the syntax.
 * Subclasses should edit {@code root} by using {@link SyntaxNodeBuilder#then(SyntaxNodeBuilder)}, {@link SyntaxNodeBuilder#split(SyntaxNodeBuilder...)}, etc
 *
 * @see SyntaxNode
 */
@Legacy
public abstract class Syntax {

    protected RootSyntaxNodeBuilder rootNode = new RootSyntaxNodeBuilder(this);
    @NotNull
    private List<SyntaxNode> currentNodes = new ArrayList<>();
    final Map<Class<? extends TokenType>, List<Token>> inputTokens = new HashMap<>();
    @NotNull
    private final List<Syntax> nextExpectedSyntaxes = new ArrayList<>();
    private RootSyntaxNode builtRoot;
    final List<SyntaxNode> route = new ArrayList<>();



    public Map<Class<? extends TokenType>, List<Token>> getInputTokens() {
        return inputTokens;
    }
    public static String getName() {
        throw new IllegalStateException("All subclasses of Command must overwrite this method");
    }

    protected final void addNextExpectedSyntax(Syntax nextSyntax) {
        nextExpectedSyntaxes.add(nextSyntax);
    }

    @NotNull
    public final List<Syntax> getNextExpectedSyntaxes() {
        return nextExpectedSyntaxes;
    }

    @NotNull
    public List<SyntaxNode> getExpectedPatterns() {
        return currentNodes;
    }

    @NotNull
    public SyntaxNode getRootOrBuild() {
        if (builtRoot == null) builtRoot = rootNode.build();
        return builtRoot;
    }

    public ParserStatusVisitor createVisitor(DeclaredNode containingToken) {
        return new EmptyParserVisitor();
    }

    public enum Result {

        NO_MATCH,
        MATCHES_SO_FAR,
        MATCHES_EXACT

    }

    public final Result matchNext(@NotNull Token token,
                                  @Nullable Token nextToken,
                                  @NotNull DeclaredNode context,
                                  @NotNull Parser.Status status) {
        if (!isValidInContext(context)) return Result.NO_MATCH;
        if (!isValidInStatus(status)) return Result.NO_MATCH;
        if (currentNodes.size() == 0) {
            currentNodes.add(getRootOrBuild());
        }

        Result matchResult = Result.NO_MATCH;

        List<SyntaxNode> nextNodes = new ArrayList<>(currentNodes.size());

        for (SyntaxNode node : currentNodes) {
            SyntaxNode.MatchResult nodeMatchResult = node.getNextNodeMatching(token, nextToken, context, status);
            LogUtils.warn("syntax {}\nnodeMatchResult: {}, node: {}, token: {}, peeking: {}",
                    getClass().getName(),
                    nodeMatchResult,
                    node.representData(),
                    token,
                    nextToken);

            SyntaxNode nextNode = nodeMatchResult.nextNode();
            SyntaxNode subNode = nodeMatchResult.subNode();
            Result success = nodeMatchResult.result();
            if (nextNodes.contains(nextNode)) continue;

            if (success == Result.NO_MATCH)
                continue;


            if (success == Result.MATCHES_EXACT) {
                if (subNode instanceof LoopingSyntaxNode looper && !route.contains(looper) || subNode != null) {
                    LogUtils.success("current {} syntax {} is running tasks \n{}\n for {}; it matches exact", node.representData(), getClass().getSimpleName(), nextNode.tasks, nextNode.representData());
                    subNode.runTasks();
                }
                if (!route.contains(nextNode)) route.add(nextNode);
                return Result.MATCHES_EXACT;
            }

            if (success == Result.MATCHES_SO_FAR) {
                if (subNode instanceof LoopingSyntaxNode looper && !route.contains(looper) || subNode != null) {
                    LogUtils.success("current {} syntax {} is running tasks \n{}\n for {}; it matches so far", node.representData(), getClass().getSimpleName(), nextNode.tasks, nextNode.representData());
                    subNode.runTasks();
                }

                nextNodes.add(nextNode);
                if (!route.contains(nextNode)) route.add(nextNode);

                matchResult = Result.MATCHES_SO_FAR;
                // TODO: 2022-10-27 add "nextNode.runTasksOnSuccess()" here
            }

        }

        currentNodes = nextNodes;
        return matchResult;
    }


    protected abstract boolean isValidInStatus(Parser.Status status);

    protected abstract boolean isValidInContext(DeclaredNode context);

}
