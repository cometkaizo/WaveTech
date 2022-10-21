package me.cometkaizo.wavetech.parser.syntaxes.nodes;

import me.cometkaizo.wavetech.lexer.tokens.TokenType;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

public abstract class SyntaxNodeBuilder {

    Syntax syntax;
    final Set<SyntaxNodeBuilder> subNodes = new LinkedHashSet<>(0);
    final Set<Runnable> tasks = new LinkedHashSet<>(0);
    boolean merges = false;
    boolean splits = false;
    final boolean acceptsToken;
    /**
     * Represents the last node of our line of nodes. When a line splits and merges again, {@code focus} will be the merged node.
     * In a root command node, {@code focus} will always be the last node in the graph. Under normal circumstances, there should
     * be no need to ever access an inaccurate {@code focus}.
     */
    @NotNull
    private SyntaxNodeBuilder focus = this;


    public SyntaxNodeBuilder() {
        this.acceptsToken = !(this instanceof SoftSyntaxNodeBuilder);
    }

    protected abstract SyntaxNode build();

    public final SyntaxNodeBuilder executes(Runnable task) {
        focus.tasks.add(task);
        return this;
    }

    public final SyntaxNodeBuilder then(@NotNull SyntaxNodeBuilder subNode) {
        focus.addSubNodeToEndOfLine(subNode);
        focus = subNode.focus;
        return this;
    }
    public final SyntaxNodeBuilder then(@NotNull TokenType type) {
        then(new TokenTypeSyntaxNodeBuilder(type));
        return this;
    }
    public final SyntaxNodeBuilder then(@NotNull TokenType type, @NotNull TokenType transformedType) {
        then(new TokenTypeSyntaxNodeBuilder(type, transformedType));
        return this;
    }

    public final SyntaxNodeBuilder split(SyntaxNodeBuilder... subNodes) {
        if (subNodes.length == 1) {
            then(subNodes[0]);
            return this;
        }

        List<SyntaxNodeBuilder> subNodesList = new ArrayList<>(Arrays.asList(subNodes));
        boolean includeNone = subNodesList.contains(null);

        split(includeNone, subNodesList);

        return this;
    }
    public final SyntaxNodeBuilder split(TokenType... types) {
        if (types.length == 1) {
            then(types[0]);
            return this;
        }

        boolean includeNone = Arrays.asList(types).contains(null);

        split(includeNone, Arrays.stream(types)
                .map(tokenType -> {
                    if (tokenType == null) return null;
                    return new TokenTypeSyntaxNodeBuilder(tokenType);
                })
                .map(o -> (SyntaxNodeBuilder)o)
                .collect(Collectors.toCollection(ArrayList::new)));
        return this;
    }

    private void split(boolean includeNone, List<SyntaxNodeBuilder> subNodes) {
        focus.splits = true;

        // always merge after splitting
        SyntaxNodeBuilder mergeDestination = new EmptySyntaxNodeBuilder();

        subNodes.removeIf(Objects::isNull);

        for (SyntaxNodeBuilder subNode : subNodes) {
            subNode.addSubNodeToEndOfLine(mergeDestination);
            subNode.focus.merges = true;
            focus.addSubNode(subNode);
            //LogUtils.debug("added subNode {} to {} (syntax {})", subNode, focus, focus.syntax);
        }

        if (includeNone) {
            SyntaxNodeBuilder none = new EmptySyntaxNodeBuilder();
            none.addSubNodeToEndOfLine(mergeDestination);
            none.merges = true;
            focus.addSubNode(none);
            //LogUtils.debug("includesNone, added subNode {} to {} (syntax {})", none, focus, focus.syntax);
        }
        focus = mergeDestination;

    }

    private void addSubNodeToEndOfLine(SyntaxNodeBuilder subNode) {
        focus.addSubNode(subNode);
    }

    private void addSubNode(SyntaxNodeBuilder subNode) {
        throwIfNodeIsRoot(subNode);
        throwIfThisNotEndOfLine(subNode);

        subNode.setSyntax(syntax);
        subNodes.add(subNode);
    }

    private void setSyntax(Syntax syntax) {
        this.syntax = syntax;
        for (SyntaxNodeBuilder subNode : subNodes) {
            if (subNode.syntax != this.syntax) subNode.setSyntax(syntax);
        }
    }

    private static void throwIfNodeIsRoot(SyntaxNodeBuilder node) {
        if (node.isRoot())
            throw new IllegalArgumentException("Cannot add a root command node as a sub-node: \n" + node);
    }

    private void throwIfThisNotEndOfLine(SyntaxNodeBuilder subNode) {
        if (!splits && subNodes.size() == 1)
            throw new IllegalStateException("Cannot add multiple sub-nodes to a non-splitting node; attempted to add \n" +
                    subNode + "\nto node \n" + this + "\nwith sub-node \n" + subNodes);
    }
    public final boolean isRoot() {
        return this instanceof RootSyntaxNodeBuilder;
    }



    public boolean hasTokenAcceptingSubNodes() {
        if (subNodes.size() == 0) return false;
        for (SyntaxNodeBuilder subNode : subNodes) {
            if (subNode.acceptsToken) return true;
            else if (subNode.hasTokenAcceptingSubNodes()) return true;
        }
        return false;
    }
}
