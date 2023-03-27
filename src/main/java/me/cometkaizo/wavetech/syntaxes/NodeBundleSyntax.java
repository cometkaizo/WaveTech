package me.cometkaizo.wavetech.syntaxes;

import org.jetbrains.annotations.NotNull;

public class NodeBundleSyntax extends Syntax<SyntaxStructure> {

    NodeBundleSyntax(SyntaxNodeBuilder... nodes) {
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
