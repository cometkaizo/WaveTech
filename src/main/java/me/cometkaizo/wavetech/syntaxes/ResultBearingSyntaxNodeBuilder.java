package me.cometkaizo.wavetech.syntaxes;

import java.util.Arrays;
import java.util.HashSet;

public abstract class ResultBearingSyntaxNodeBuilder<T> extends SyntaxNodeBuilder {

    protected final HashSet<String> resultLabels;

    public ResultBearingSyntaxNodeBuilder(String... labels) {
        resultLabels = new HashSet<>(Arrays.asList(labels));
    }

    public ResultBearingSyntaxNodeBuilder<T> withLabel(String label) {
        if (label != null)
            resultLabels.add(label);
        return this;
    }

    @Override
    protected abstract ResultBearingSyntaxNode<T> build();
}
