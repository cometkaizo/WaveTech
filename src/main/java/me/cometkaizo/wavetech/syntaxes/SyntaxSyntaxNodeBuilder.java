package me.cometkaizo.wavetech.syntaxes;

import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class SyntaxSyntaxNodeBuilder<T extends SyntaxStructure> extends ResultBearingSyntaxNodeBuilder<SyntaxStructure> {
    @NotNull
    protected final Supplier<? extends @NotNull Syntax<? extends T>> syntaxSupplier;

    public SyntaxSyntaxNodeBuilder(@NotNull Supplier<? extends @NotNull Syntax<? extends T>> syntax) {
        this.syntaxSupplier = syntax;
    }

    public SyntaxSyntaxNodeBuilder(String label, @NotNull Supplier<? extends @NotNull Syntax<? extends T>> syntax) {
        super(label);
        this.syntaxSupplier = syntax;
    }

    @Override
    protected SyntaxSyntaxNode<T> build() {
        return new SyntaxSyntaxNode<>(this);
    }

}
