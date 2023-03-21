package me.cometkaizo.wavetech.syntaxes;

import me.cometkaizo.util.CollectionUtils;
import me.cometkaizo.util.PeekingIterator;
import me.cometkaizo.wavetech.lexer.tokens.Token;

import java.util.List;

/**
 * A SyntaxNode represents a single expected thing in a syntax (e.g. keywords, symbols, modifiers, etc).
 * Specific functionality is specified by subclasses.
 *
 * @see Syntax
 * @see RootSyntaxNode
 * @see TokenTypeSyntaxNode
 * @see SoftSyntaxNode
 * @see ConditionalSyntaxNode
 */
public abstract class SyntaxNode {

    final List<Syntax<?>> syntaxes;
    protected final List<SyntaxNode> subNodes;
    protected final List<Runnable> tasks;

    // metadata
    final boolean splits;
    final boolean merges;
    private final int depth;

    protected SyntaxNode(SyntaxNodeBuilder builder) {
        this.subNodes = buildSubNodes(builder);
        this.tasks = List.of(builder.tasks.toArray(Runnable[]::new));


        this.syntaxes = builder.syntaxes;
        this.splits = builder.splits;
        this.merges = builder.merges;
        this.depth = builder.calculateDepth();
    }

    private List<SyntaxNode> buildSubNodes(SyntaxNodeBuilder builder) {
        return CollectionUtils.map(builder.subNodes, SyntaxNodeBuilder::build);
    }

    public void runTasks() {
        for (Runnable task : tasks) {
            task.run();
        }
    }


    /**
     * Tries to apply candidate tokens to this syntax node. If successful, the iterator will be advanced appropriately.
     * If not successful, the iterator <strong>may remain changed</strong>.
     * <i>If not successful, changes to iterator should be reverted.</i>
     *
     * @param tokens       the list of tokens
     * @param structure    the structure to store in. Note: If the structure is the wrong type it may produce unwanted behavior
     * @param matcher      the syntax matcher used to match this syntax
     * @return whether this node matched successfully.
     */
    protected abstract boolean matchAndStore(PeekingIterator<Token> tokens, SyntaxStructure structure, SyntaxMatcher matcher);

    public boolean hasSubNodes() {
        return subNodes.size() > 0;
    }

    public abstract boolean equals(Object o);

    public abstract int hashCode();

    public String toPrettyString() {
        return getClass().getSimpleName().replaceAll("(?<=.)SyntaxNode$", "").toUpperCase();
    }

    public int getDepth() {
        return depth;
    }

}
