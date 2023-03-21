package me.cometkaizo.wavetech.syntaxes;

import org.jetbrains.annotations.Nullable;

/**
 * The Syntax class represents a way to parse a series of tokens into {@link SyntaxStructure}s.
 * Subclasses define what types of tokens are to be expected, and what to do with them.
 * The {@link RootSyntaxNode} {@code root} field is the root of a node tree which specifies the syntax.
 * Subclasses should edit {@code root} by using {@link SyntaxNodeBuilder#then(SyntaxNodeBuilder)}, {@link SyntaxNodeBuilder#split(SyntaxNodeBuilder...)}, etc
 *
 * @see SyntaxNode
 */
public abstract class Syntax<T extends SyntaxStructure> {

    protected final RootSyntaxNodeBuilder rootBuilder = new RootSyntaxNodeBuilder(this);
    private RootSyntaxNode root;
    @Nullable
    public abstract T getStructure(@Nullable SyntaxStructure parent);

    public RootSyntaxNode getRootNode() {
        if (root == null) root = rootBuilder.build();
        return root;
    }
}
