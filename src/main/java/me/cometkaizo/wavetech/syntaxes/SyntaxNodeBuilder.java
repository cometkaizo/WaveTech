package me.cometkaizo.wavetech.syntaxes;

import me.cometkaizo.logging.LogUtils;
import me.cometkaizo.util.CollectionUtils;
import me.cometkaizo.util.NullableOptional;
import me.cometkaizo.wavetech.lexer.tokens.TokenType;
import me.cometkaizo.wavetech.parser.syntaxes.ClassAccessSyntax;
import me.cometkaizo.wavetech.parser.syntaxes.IdentifierSyntax;
import me.cometkaizo.wavetech.parser.structures.ClassAccessor;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public abstract class SyntaxNodeBuilder {

    final List<Syntax<?>> syntaxes = new ArrayList<>(1);
    final List<SyntaxNodeBuilder> subNodes = new ArrayList<>(3);
    final List<Runnable> tasks = new ArrayList<>(0);
    boolean merges = false;
    boolean splits = false;

    @NotNull
    protected SyntaxNodeBuilder farthestAncestor = this;


    public SyntaxNodeBuilder() {

    }

    protected abstract SyntaxNode build();

    public final SyntaxNodeBuilder executes(Runnable task) {
        tasks.add(task);
        return this;
    }
    public final SyntaxNodeBuilder log(String message) {
        return executes(() -> LogUtils.debug(message));
    }

    /**
     * Adds a non-null sub-node to the current focus. This method cannot add optional nodes; for that use {@link SyntaxNodeBuilder#optionally(SyntaxNodeBuilder)}.
     * @param node the sub-node to add
     * @return the sub-node, if added successfully
     */
    public final SyntaxNodeBuilder then(@NotNull SyntaxNodeBuilder node) {
        var subNode = node.farthestAncestor;
        throwIfNodeIsThis(subNode);
        if (subNode.isRoot()) throw new IllegalArgumentException("Cannot add root as sub-node");

        addSubNode(subNode);
        return subNode;
    }

    public final TokenTypeSyntaxNodeBuilder then(@NotNull TokenType type) {
        return (TokenTypeSyntaxNodeBuilder) then(new TokenTypeSyntaxNodeBuilder(type));
    }

    public final TokenTypeSyntaxNodeBuilder then(String label, @NotNull TokenType type) {
        var subNode = new TokenTypeSyntaxNodeBuilder(type);
        subNode.withLabel(label);
        return (TokenTypeSyntaxNodeBuilder) then(subNode);
    }

    @SuppressWarnings("unchecked")
    public <T extends SyntaxStructure> SyntaxSyntaxNodeBuilder<T> then(Supplier<? extends Syntax<T>> syntax) {
        var subNode = new SyntaxSyntaxNodeBuilder<>(syntax);
        return (SyntaxSyntaxNodeBuilder<T>) then(subNode);
    }

    public <T extends SyntaxStructure> SyntaxSyntaxNodeBuilder<T> then(String label, Supplier<? extends Syntax<T>> syntax) {
        var subNode = new SyntaxSyntaxNodeBuilder<>(syntax);
        return (SyntaxSyntaxNodeBuilder<T>) then(label, subNode);
    }

    @SuppressWarnings("unchecked")
    public <T> ResultBearingSyntaxNodeBuilder<T> then(String label, ResultBearingSyntaxNodeBuilder<T> subNode) {
        subNode.withLabel(label);
        return (ResultBearingSyntaxNodeBuilder<T>) then(subNode);
    }

    public SyntaxNodeBuilder thenIdentifier() {
        return then(IdentifierSyntax::getInstance);
    }
    public SyntaxNodeBuilder thenIdentifier(String label) {
        return then(label, IdentifierSyntax::getInstance);
    }

    public SyntaxSyntaxNodeBuilder<ClassAccessor> thenType() {
        return then(ClassAccessSyntax::getInstance);
    }

    /**
     * <p>Splits the focus node into multiple branches. {@code null} can be used to signify the branches being optional.</p>
     * Note: multiple {@code null}s will not have additional effect.
     * @param subNodes the sub-nodes to branch from the focus node. Can include {@code null}
     * @return this node-builder object
     */
    public final EmptySyntaxNodeBuilder split(SyntaxNodeBuilder... subNodes) {
        if (subNodes.length < 2)
            throw new IllegalArgumentException("Cannot split with " + subNodes.length + " argument! Try using SyntaxNodeBuilder#then(SyntaxNodeBuilder node) instead");

        boolean includeNone = CollectionUtils.arrayContains(subNodes, null);

        return split(includeNone, subNodes);
    }
    public final EmptySyntaxNodeBuilder split(TokenType... types) {
        if (types.length < 2)
            throw new IllegalArgumentException("Cannot split with " + types.length + " argument! Try using SyntaxNodeBuilder#then(SyntaxNodeBuilder node) instead");

        boolean includeNone = Arrays.asList(types).contains(null);
        var subNodes = CollectionUtils.map(types, tokenType -> {
            if (tokenType == null) return null;
            return new TokenTypeSyntaxNodeBuilder(tokenType);
        }, TokenTypeSyntaxNodeBuilder[]::new);

        return split(includeNone,
                subNodes
        );
    }
    public final EmptySyntaxNodeBuilder split(String label, TokenType... types) {
        var subNodes = CollectionUtils.map(types, TokenTypeSyntaxNodeBuilder::new, TokenTypeSyntaxNodeBuilder[]::new);
        return split(label, subNodes);
    }
    @SafeVarargs
    public final <T extends ResultBearingSyntaxNodeBuilder<?>> EmptySyntaxNodeBuilder split(String label, T... subNodes) {
        CollectionUtils.forEach(subNodes, subNode -> subNode.withLabel(label));
        return split(subNodes);
    }
    @SafeVarargs
    public final EmptySyntaxNodeBuilder split(String label, Supplier<? extends Syntax<? extends SyntaxStructure>>... suppliers) {
        var nodes = CollectionUtils.map(suppliers, SyntaxSyntaxNodeBuilder::new, SyntaxSyntaxNodeBuilder[]::new);
        return split(label, nodes);
    }

    private EmptySyntaxNodeBuilder split(boolean optional, SyntaxNodeBuilder... subNodesArray) {
        splits = true;
        var subNodes = new ArrayList<>(Arrays.asList(subNodesArray));


        throwIfInvalidSubNodes(subNodes);
        removeNull(subNodes);

        // always merge after splitting
        EmptySyntaxNodeBuilder mergeDestination = new EmptySyntaxNodeBuilder();

        mergeEndsAndAdd(subNodes, mergeDestination);

        if (optional) {
            SyntaxNodeBuilder none = new EmptySyntaxNodeBuilder();
            none.addSubNode(mergeDestination);
            none.merges = true;
            addSubNode(none);
        }

        mergeDestination.farthestAncestor = this.farthestAncestor;
        return mergeDestination;

    }

    private void mergeEndsAndAdd(ArrayList<SyntaxNodeBuilder> subNodes, EmptySyntaxNodeBuilder mergeDestination) {
        for (SyntaxNodeBuilder farthestDescendant : subNodes) {

            if (!farthestDescendant.merges) {
                farthestDescendant.addSubNode(mergeDestination);
                farthestDescendant.merges = true;
            }

            var subNode = farthestDescendant.farthestAncestor;
            addSubNode(subNode);
        }
    }

    private void throwIfInvalidSubNodes(List<SyntaxNodeBuilder> subNodes) {
        boolean onlyContainsNull = true;
        for (var node : subNodes) {
            if (onlyContainsNull && node != null) onlyContainsNull = false;
            if (node == null) continue;

            throwIfNodeIsThis(node);
            throwIfNodeIsRoot(node);
        }

        if (onlyContainsNull) throw new IllegalArgumentException("No sub-node to add");
    }

    private void throwIfNodeIsThis(SyntaxNodeBuilder node) {
        if (this == node) throw new IllegalArgumentException("Cannot add self as sub-node");
    }

    private static void removeNull(List<SyntaxNodeBuilder> subNodes) {
        subNodes.removeIf(Objects::isNull);
    }



    /**
     * Adds an optional sub-node
     * @param subNode the optional sub-node
     * @return this node-builder object
     */
    public final EmptySyntaxNodeBuilder optionally(SyntaxNodeBuilder subNode) {
        return split(true, subNode);
    }

    /**
     * Adds an optional sub-node with a label.
     * <p>
     * The code
     * <pre>
     *     SyntaxNodeBuilder builder = ...
     *     builder.optionally("label", new TokenTypeSyntaxNodeBuilder(VOID));
     * </pre>
     * Is equivalent to
     * <pre>
     *     builder.then(new TokenTypeSyntaxNodeBuilder(VOID).withLabel("label"));
     * </pre>
     *
     * @apiNote this adds the label to the final nodes passed in as parameters (as opposed to the highest node).
     * For example, the code
     * <pre>
     *     builder.optionally("label", new TokenTypeSyntaxNodeBuilder(VOID).then(new TokenTypeSyntaxNodeBuilder(VOID)));
     * </pre>
     * Is equivalent to
     * <pre>
     *     builder.then(new TokenTypeSyntaxNodeBuilder(VOID).then(new TokenTypeSyntaxNodeBuilder(VOID)<b>.withLabel("label")</b>));
     * </pre>
     * and not
     * <pre>
     *     builder.then(new TokenTypeSyntaxNodeBuilder(VOID)<b>.withLabel("label")</b>.then(new TokenTypeSyntaxNodeBuilder(VOID)));
     * </pre>
     * @param label the label to apply to the sub-nodes
     * @param subNodes the sub-nodes to add
     * @return this node-builder object
     */
    public final EmptySyntaxNodeBuilder optionally(String label, ResultBearingSyntaxNodeBuilder<?>... subNodes) {
        for (var subNode : subNodes) {
            subNode.withLabel(label);
        }
        return optionally(subNodes);
    }
    @SafeVarargs
    public final EmptySyntaxNodeBuilder optionally(String label, Supplier<? extends Syntax<? extends SyntaxStructure>>... suppliers) {
        var nodes = CollectionUtils.map(suppliers, SyntaxSyntaxNodeBuilder::new, SyntaxSyntaxNodeBuilder[]::new);
        return optionally(label, nodes);
    }

    public final EmptySyntaxNodeBuilder optionally(SyntaxNodeBuilder... subNodes) {
        CollectionUtils.requireNoNullElement(subNodes, "Cannot add null as sub-node");

        return split(true, subNodes);
    }
    public final EmptySyntaxNodeBuilder optionally(TokenType... types) {
        return optionally(
                CollectionUtils.map(types, TokenTypeSyntaxNodeBuilder::new, TokenTypeSyntaxNodeBuilder[]::new)
        );
    }
    public final EmptySyntaxNodeBuilder optionally(String label, TokenType... types) {
        var subNodes = CollectionUtils.map(types, TokenTypeSyntaxNodeBuilder::new, TokenTypeSyntaxNodeBuilder[]::new);

        for (var subNode : subNodes) {
            subNode.withLabel(label);
        }

        return optionally(subNodes);
    }

    @SafeVarargs
    public final <T extends SyntaxNodeBuilder> EmptySyntaxNodeBuilder optionally(Consumer<T> operation, T... subNodes) {
        CollectionUtils.forEach(subNodes, operation);
        return optionally(subNodes);
    }
    public final EmptySyntaxNodeBuilder optionally(Consumer<TokenTypeSyntaxNodeBuilder> operation, TokenType... types) {
        CollectionUtils.requireNoNullElement(types, "Cannot add null as sub-node");

        var subNodes = CollectionUtils.map(types, TokenTypeSyntaxNodeBuilder::new, TokenTypeSyntaxNodeBuilder[]::new);
        CollectionUtils.forEach(subNodes, operation);

        return split(true,
                subNodes
        );
    }

    @SafeVarargs
    public final <T extends SyntaxStructure>
    CustomSyntaxNodeBuilder<Void> zeroOrMore(Supplier<? extends Syntax<? extends T>>... syntaxes) {
        var syntaxNodes = CollectionUtils.map(syntaxes, SyntaxSyntaxNodeBuilder::new, SyntaxSyntaxNodeBuilder[]::new);
        var node = createLoop(0, -1, syntaxNodes);
        then(node);
        return node;
    }

    @SafeVarargs
    public final <T extends SyntaxStructure>
    CustomSyntaxNodeBuilder<Void> zeroOrMore(String label, Supplier<? extends Syntax<? extends T>>... syntaxes) {
        var syntaxNodes = CollectionUtils.map(syntaxes, SyntaxSyntaxNodeBuilder::new, SyntaxSyntaxNodeBuilder[]::new);
        for (var syntaxNode : syntaxNodes) syntaxNode.withLabel(label);

        var node = createLoop(0, -1, syntaxNodes);
        then(node);
        return node;
    }

    @SafeVarargs
    public final <T>
    CustomSyntaxNodeBuilder<Void> zeroOrMore(ResultBearingSyntaxNodeBuilder<? extends T>... nodes) {
        var node = createLoop(0, -1, nodes);
        then(node);
        return node;
    }
    @SafeVarargs
    public final <T>
    CustomSyntaxNodeBuilder<Void> zeroOrMore(String label, ResultBearingSyntaxNodeBuilder<? extends T>... nodes) {
        for (var node : nodes) node.withLabel(label);

        var node = createLoop(0, -1, nodes);
        then(node);
        return node;
    }

    @NotNull
    private static
    CustomSyntaxNodeBuilder<Void> createLoop(int min, int max, ResultBearingSyntaxNodeBuilder<?>[] nodes) {
        if (nodes.length == 0) throw new IllegalArgumentException("No Nodes to loop!");


        var nodeSyntax = new NodeBundleSyntax<>(nodes);
        return new CustomSyntaxNodeBuilder<>((tokens, parent, matcher) -> {
            if (!tokens.hasNext()) return null;
            int startCursor = tokens.cursor();

            for (int iterationCount = 0; max < 0 || iterationCount != max + 1; iterationCount ++) {
                int previousCursor = tokens.cursor();

                var futureResult = matcher.tryMatch(nodeSyntax, parent, null);

                if (futureResult == null) {
                    if (iterationCount >= min) {
                        tokens.jumpTo(previousCursor);
                        return NullableOptional.of(null);
                    } else {
                        tokens.jumpTo(startCursor);
                        return NullableOptional.empty();
                    }
                }

            }

            tokens.jumpTo(startCursor);
            return null;
        });
    }

    public AnchorSyntaxNodeBuilder anchorStart() {
        var anchorNode = new AnchorSyntaxNodeBuilder(0);
        then(anchorNode);
        return anchorNode;
    }

    public AnchorSyntaxNodeBuilder anchorEnd() {
        var anchorNode = new AnchorSyntaxNodeBuilder(-1);
        then(anchorNode);
        return anchorNode;
    }

    private static class NodeBundleSyntax<T> extends Syntax<SyntaxStructure> {

        @SafeVarargs
        NodeBundleSyntax(ResultBearingSyntaxNodeBuilder<? extends T>... nodes) {
            if (nodes.length == 1)
                rootBuilder.then(nodes[0]);
            else
                rootBuilder.split(nodes);
        }

        @Override
        public @NotNull SyntaxStructure getStructure(SyntaxStructure parent) {
            return parent;
        }
    }



    private void addSubNode(SyntaxNodeBuilder subNode) {
        throwIfNodeIsRoot(subNode);
        throwIfThisNotEndOfLine(subNode);

        subNode.farthestAncestor = this.farthestAncestor;
        subNode.addSyntaxes(syntaxes);
        subNodes.add(subNode);
    }
    private static void throwIfNodeIsRoot(SyntaxNodeBuilder node) {
        if (node.isRoot())
            throw new IllegalArgumentException("Cannot add a root node as a sub-node: \n" + node);
    }

    protected void addSyntax(Syntax<?> syntax) {
        this.syntaxes.add(syntax);
        for (var subNode : subNodes) {
            if (subNode.syntaxes != this.syntaxes) subNode.addSyntax(syntax);
        }
    }

    private void addSyntaxes(List<Syntax<?>> syntax) {
        this.syntaxes.addAll(syntax);
        for (var subNode : subNodes) {
            if (subNode.syntaxes.size() < this.syntaxes.size()) subNode.addSyntaxes(syntax);
        }
    }

    private void throwIfThisNotEndOfLine(SyntaxNodeBuilder subNode) {
        if (!splits && subNodes.size() == 1) {
            throw new IllegalStateException("Cannot add multiple sub-nodes to a non-splitting node; attempted to add \n" +
                    subNode + "\nto node \n" + this + " in syntax " + syntaxes + "\nwith sub-nodes \n" + subNodes);
        }
    }
    public final boolean isRoot() {
        return this instanceof RootSyntaxNodeBuilder;
    }

    public int calculateDepth() {
        if (subNodes.size() == 0) return 0;

        int maxSubNodeDepth = 0;
        for (SyntaxNodeBuilder subNode : subNodes) {
            int subNodeDepth = subNode.calculateDepth();
            if (subNodeDepth > maxSubNodeDepth)
                maxSubNodeDepth = subNodeDepth;
        }

        return maxSubNodeDepth + 1;
    }

    public String deepToString() {
        return SyntaxNodeBuilder.class.getSimpleName() + " > " + getClass().getSimpleName() + "{subNodes:" +
                (subNodes.size() > 0 ? subNodes.stream()
                        .map(syntaxNode -> syntaxNode.deepToString().indent(2))
                        .collect(Collectors.joining("\n", "\n", "")) : "none") +
                "\n  farthest ancestor: " + farthestAncestor +
                "\n}";
    }
}
