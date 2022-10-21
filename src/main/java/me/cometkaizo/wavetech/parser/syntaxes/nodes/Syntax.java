package me.cometkaizo.wavetech.parser.syntaxes.nodes;

import me.cometkaizo.wavetech.lexer.tokens.Token;
import me.cometkaizo.wavetech.lexer.tokens.TokenType;
import me.cometkaizo.wavetech.parser.Parser;
import me.cometkaizo.wavetech.parser.nodes.DeclaredNode;
import me.cometkaizo.wavetech.parser.nodes.Node;
import org.jetbrains.annotations.NotNull;

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
public abstract class Syntax {

    protected RootSyntaxNodeBuilder rootNode = new RootSyntaxNodeBuilder(this);
    protected boolean success = true;
    private List<SyntaxNode> currentNodes = new ArrayList<>();
    final Map<Class<? extends TokenType>, List<Token>> inputTokens = new HashMap<>();
    private final List<Syntax> nextExpectedPatterns = new ArrayList<>();
    private RootSyntaxNode builtRoot;

    public boolean getSuccess() {
        return success;
    }

    public Map<Class<? extends TokenType>, List<Token>> getInputTokens() {
        return inputTokens;
    }
    public static String getName() {
        throw new IllegalStateException("All subclasses of Command must overwrite this method");
    }

    protected final void addNextExpectedSyntax(Syntax nextSyntax) {
        nextExpectedPatterns.add(nextSyntax);
    }

    public final List<Syntax> getNextExpectedSyntaxes() {
        return nextExpectedPatterns;
    }

    public List<SyntaxNode> getExpectedPatterns() {
        return currentNodes;
    }

    public SyntaxNode getRoot() {
        return builtRoot;
    }

    public enum Result {

        NO_RESULT,
        MATCHES_SO_FAR,
        MATCHES_EXACT

    }

    public final Result matchNext(@NotNull DeclaredNode context, @NotNull Parser.Status status, @NotNull Token nextToken) {
        if (!isValidInContext(context)) return Result.NO_RESULT;
        if (!isValidInStatus(status)) return Result.NO_RESULT;
        if (currentNodes.size() == 0) {
            builtRoot = rootNode.build();
            currentNodes.add(builtRoot);
        }

        Result matchResult = Result.NO_RESULT;

        List<SyntaxNode> nextNodes = new ArrayList<>(currentNodes.size());

        for (SyntaxNode node : currentNodes) {
            SyntaxNode nextNode = node.getSubNodeMatching(nextToken);

            if (nextNode != null && !nextNode.hasTokenAcceptingSubNodes()) {
                return Result.MATCHES_EXACT;
            }
            if (nextNode != null && nextNode.hasTokenAcceptingSubNodes()) {
                nextNodes.add(nextNode);
                matchResult = Result.MATCHES_SO_FAR;
            }
        }

        currentNodes = nextNodes;
        //LogUtils.debug("result: {}, current nodes now: {}", matchResult, currentNodes);
        return matchResult;
    }


    protected abstract boolean isValidInStatus(Parser.Status status);

    protected abstract boolean isValidInContext(DeclaredNode context);

}
