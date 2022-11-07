package me.cometkaizo.temp.syntaxes.nodes;

import me.cometkaizo.annotations.Legacy;
import me.cometkaizo.util.LogUtils;
import me.cometkaizo.wavetech.lexer.tokens.Token;
import me.cometkaizo.wavetech.lexer.tokens.TokenType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Legacy
public abstract class SyntaxNodeBuilder {

    Syntax syntax;
    final List<SyntaxNodeBuilder> subNodes = new ArrayList<>();
    final List<Runnable> tasks = new ArrayList<>();
    final List<MatcherFunction> peekers = new ArrayList<>();
    final List<Function<Token, Boolean>> failConditions = new ArrayList<>();
    boolean merges = false;
    boolean splits = false;
    final boolean acceptsToken;
    /**
     * Represents the last node of our line of nodes. When a line splits and merges again, {@code focus} will be the merged node.
     * In a root command node, {@code focus} will always be the last node in the graph. Under normal circumstances, there should
     * be no need to ever access an inaccurate {@code focus}.
     */
    @NotNull
    protected SyntaxNodeBuilder focus = this;


    public SyntaxNodeBuilder() {
        this.acceptsToken = !(this instanceof SoftSyntaxNodeBuilder);
    }

    protected abstract SyntaxNode build();

    public final SyntaxNodeBuilder executes(Runnable task) {
        focus.tasks.add(task);
        return this;
    }

    /**
     * Adds a non-null sub-node to the current focus. This method cannot add optional nodes; for that use {@link SyntaxNodeBuilder#optionally(SyntaxNodeBuilder)}.
     * @param subNode the sub-node to add
     * @return this node-builder object
     */
    public final SyntaxNodeBuilder then(@NotNull SyntaxNodeBuilder subNode) {
        if (this.equals(subNode))
            throw new IllegalArgumentException("Cannot add self as sub-node");
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
    @Deprecated
    public final SyntaxNodeBuilder then(@NotNull Syntax syntax) {
        SyntaxSyntaxNodeBuilder subNode = new SyntaxSyntaxNodeBuilder(syntax);
        then(subNode);
        return this;
    }

    public SyntaxNodeBuilder thenSyntax(Syntax syntax) {
        forEachNextToken(new MatcherFunction("syntax:" + syntax.getClass().getName()) {

            @Override
            protected @NotNull Syntax.Result match(@NotNull Token token, @Nullable Token nextToken) {
                Syntax.Result result = matchNextFor(syntax);
                LogUtils.info("syntax {} matches {}? {}", syntax.getClass().getSimpleName(), token, result);
                return result;
            }

        });

        return this;
    }

    /**
     * <p>Splits the focus node into multiple branches. {@code null} can be used to signify the branches being optional.</p>
     * Note: multiple {@code null}s will not have additional effect.
     * @param subNodes the sub-nodes to branch from the focus node. Can include {@code null}
     * @return this node-builder object
     */
    public final SyntaxNodeBuilder split(SyntaxNodeBuilder... subNodes) {
        if (subNodes.length == 1) {
            SyntaxNodeBuilder subNode = subNodes[0];
            if (subNode == null) throw new IllegalArgumentException("Cannot add a null sub-node");
            then(subNode);
            return this;
        }

        List<SyntaxNodeBuilder> subNodesList = new ArrayList<>(Arrays.asList(subNodes));
        boolean includeNone = subNodesList.contains(null);

        split(includeNone, subNodesList);

        return this;
    }
    public final SyntaxNodeBuilder split(TokenType... types) {
        if (types.length == 1) {
            TokenType type = types[0];
            if (type == null) throw new IllegalArgumentException("Cannot add a null type");
            then(type);
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
            if (this.equals(subNode))
                throw new IllegalArgumentException("Cannot add self as sub-node");

            if (!subNode.focus.merges) {
                subNode.addSubNodeToEndOfLine(mergeDestination);
                subNode.focus.merges = true;
            }
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

    /**
     * Adds an optional value as a sub-node. This is a convenience method for {@link SyntaxNodeBuilder#split(SyntaxNodeBuilder...)};
     * the following code
     * <pre>
     *     nodeBuilder.optionally(ABSTRACT);</pre>
     *
     * is equivalent to:
     * <pre>
     *     nodeBuilder.split(ABSTRACT, null);</pre>
     *
     * @param subNode the optional sub-node
     * @return this node-builder object
     */
    public final SyntaxNodeBuilder optionally(@NotNull SyntaxNodeBuilder subNode) {
        split(subNode, null);
        return this;
    }
    public final SyntaxNodeBuilder optionally(@NotNull TokenType type) {
        split(type, null);
        return this;
    }
    public final SyntaxNodeBuilder optionally(@NotNull TokenType type, @NotNull TokenType transformedType) {
        split(new TokenTypeSyntaxNodeBuilder(type, transformedType), null);
        return this;
    }

    public final SyntaxNodeBuilder optionally(SyntaxNodeBuilder... subNodes) {
        List<SyntaxNodeBuilder> subNodeList = new ArrayList<>(List.of(subNodes));
        if (subNodeList.contains(null))
            throw new IllegalArgumentException("Cannot add null as sub-node");
        subNodeList.add(null);
        split(subNodeList.toArray(SyntaxNodeBuilder[]::new));
        return this;
    }
    public final SyntaxNodeBuilder optionally(TokenType... types) {
        List<TokenType> tokenTypeList = new ArrayList<>(List.of(types));
        if (tokenTypeList.contains(null))
            throw new IllegalArgumentException("Cannot add null as sub-node");
        tokenTypeList.add(null);
        split(tokenTypeList.toArray(TokenType[]::new));
        return this;
    }


    /**
     * Adds a single sub-node that accepts a token based on a condition. This is a convenience method for
     * <pre>
     *     rootNode.then(new ConditionalSyntaxNode(someCondition));</pre>
     * @param condition the condition of the node
     * @return this node-builder object
     */
    public final SyntaxNodeBuilder forNextToken(@NotNull Function<Token, Boolean> condition) {
        then(new ConditionalSyntaxNodeBuilder(condition));
        return this;
    }

    /**
     * A method that allows for more precise token handling. The {@code function} parameter function will be called for
     * every token after the node is reached, until some call of {@code function} returns
     * {@link Syntax.Result#MATCHES_EXACT}, in which case any sub-nodes of
     * the node will be tested as usual.
     * <p>
     * If {@code function} returns {@link Syntax.Result#NO_MATCH}
     * on any invocation, the syntax it is part of will consequently not match the input token.
     * @param function a function that, given a token {@code T} and the next token, will determine if {@code T}
     *                  is a valid candidate, whether the loop should continue or end, and whether it was successful.
     * @return this node-builder object
     */
    public final SyntaxNodeBuilder forEachNextToken(@NotNull MatcherFunction function) {
        then(new LoopingSyntaxNodeBuilder(function));
        return this;
    }
    /**
     * A method that allows for more precise token handling. The {@code condition} parameter function will be called for
     * every token after the node is reached, until some call of {@code condition} returns {@code false}.
     * <p>
     * If {@code condition} returns {@code false} on the first token checked, then the syntax will not match the token.
     * If it returns false on any subsequent checks (after it has returned {@code true} at least once), the loop will end.
     * @param condition a function that, given a token {@code T} and the next token, will determine if {@code T}
     *                  is a valid candidate.
     * @return this node-builder object
     */
    public final SyntaxNodeBuilder forEachNextToken(@NotNull Function<Token, Boolean> condition) {
        forEachNextToken(new MatcherFunction("conditional:" + condition.getClass().getName()) {

            @Override
            @NotNull
            public Syntax.Result match(@NotNull Token token, @Nullable Token nextToken) {

                if (matchesToken(token)) {
                    if (matchesToken(nextToken))
                        return Syntax.Result.MATCHES_SO_FAR;
                    else
                        return Syntax.Result.MATCHES_EXACT;
                } else {

                    return Syntax.Result.NO_MATCH;

                }
            }

            private boolean matchesToken(Token candidate) {
                return condition.apply(candidate);
            }

        });
        return this;
    }

    public final SyntaxNodeBuilder peekToken(@NotNull MatcherFunction function) {
        focus.peekers.add(function);
        return this;
    }

    public SyntaxNodeBuilder failIfNextToken(Function<Token, Boolean> condition) {
        focus.failConditions.add(condition);
        return this;
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
        if (!splits && subNodes.size() == 1) {
            throw new IllegalStateException("Cannot add multiple sub-nodes to a non-splitting node; attempted to add \n" +
                    subNode + "\nto node \n" + this + " in syntax " + syntax + "\nwith sub-nodes \n" + subNodes);
        }
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
        return SyntaxNodeBuilder.class.getSimpleName() + "$" + getClass().getSimpleName() + "{subNodes:" +
                (subNodes.size() > 0 ? subNodes.stream()
                        .map(syntaxNode -> syntaxNode.deepToString().indent(2))
                        .collect(Collectors.joining("\n", "\n", "")) : "none") +
                "}";
    }

}
