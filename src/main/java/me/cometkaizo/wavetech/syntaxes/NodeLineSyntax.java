package me.cometkaizo.wavetech.syntaxes;

import me.cometkaizo.util.CollectionUtils;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class NodeLineSyntax extends Syntax<SyntaxStructure> {

    public NodeLineSyntax(SyntaxNodeBuilder... nodes) {
        SyntaxNodeBuilder builder = rootBuilder;
        for (var node : nodes) {
            builder = builder.then(node);
        }
    }

    @SafeVarargs
    public NodeLineSyntax(Supplier<? extends Syntax<?>>... syntaxes) {
        this(CollectionUtils.map(syntaxes, SyntaxSyntaxNodeBuilder::new, SyntaxSyntaxNodeBuilder<?>[]::new));
    }

    @Override
    public @NotNull SyntaxStructure getStructure(SyntaxStructure parent) {
        return parent;
    }
}
