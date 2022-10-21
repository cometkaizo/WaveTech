package me.cometkaizo.wavetech.parser.syntaxes.nodes;

import me.cometkaizo.wavetech.lexer.tokens.Token;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A SyntaxNode represents a single expected token in a syntax (e.g. keywords, symbols, modifiers, etc).
 * Specific functionality is specified by subclasses.
 *
 * @see Syntax
 * @see RootSyntaxNode
 * @see LiteralSyntaxNode
 * @see TokenTypeSyntaxNode
 * @see SoftSyntaxNode
 * @see ConditionalSyntaxNode
 */
public abstract class SyntaxNode {

    private final Syntax syntax;
    protected final Set<SyntaxNode> subNodes;
    protected final Set<Runnable> tasks;


    private final boolean hasTokenAcceptingSubNodes;
    private final boolean acceptsToken;
    final boolean splits;
    final boolean merges;

    protected SyntaxNode(SyntaxNodeBuilder builder) {
        this.subNodes = buildSubNodes(builder);
        this.tasks = builder.tasks;
        this.syntax = builder.syntax;
        this.hasTokenAcceptingSubNodes = builder.hasTokenAcceptingSubNodes();
        this.acceptsToken = builder.acceptsToken;
        this.splits = builder.splits;
        this.merges = builder.merges;
    }

    private static Set<SyntaxNode> buildSubNodes(SyntaxNodeBuilder builder) {
        return builder.subNodes.stream()
                .map(SyntaxNodeBuilder::build)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    protected final SyntaxNode getSubNodeMatching(Token nextToken) {

        for (Runnable task : tasks)
            task.run();

        if (!hasSubNodes()) {
            return null;
        }

        if (!syntax.inputTokens.containsKey(nextToken.getType().getClass()))
            syntax.inputTokens.put(nextToken.getType().getClass(), new ArrayList<>());
        List<Token> inputTokenList = syntax.inputTokens.get(nextToken.getType().getClass());

        return getValidSubNode(nextToken, inputTokenList);
    }

    @Nullable
    private SyntaxNode getValidSubNode(Token nextToken, List<Token> inputTokenList) {
        for (SyntaxNode subNode : subNodes) {
            if (!subNode.acceptsToken()) {
                return subNode.getSubNodeMatching(nextToken);
            }
            if (subNode.accepts(nextToken)) {
                inputTokenList.add(subNode.transform(nextToken));
                return subNode;
            }
        }
        return null;
    }

    protected abstract Token transform(Token nextToken);

    public boolean acceptsToken() {
        return acceptsToken;
    }



    protected abstract boolean accepts(Token candidate);

    public boolean hasSubNodes() {
        return subNodes.size() > 0;
    }

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
}
