package me.cometkaizo.wavetech.syntaxes;

import me.cometkaizo.wavetech.analyzer.structures.StructureAnalyzer;
import me.cometkaizo.wavetech.lexer.tokens.Position;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractSyntaxStructure<T extends StructureAnalyzer> implements SyntaxStructure {

    private T analyzer;
    public Position startPosition;
    public Position endPosition;

    public @NotNull T getAnalyzer() {
        if (analyzer == null) analyzer = createAnalyzer();
        return analyzer;
    }

    protected abstract @NotNull T createAnalyzer();

    @Override
    public void setStartPosition(Position startPosition) {
        this.startPosition = startPosition;
    }
    @Override
    public void setEndPosition(Position endPosition) {
        this.endPosition = endPosition;
    }
    @Override
    public Position getStartPosition() {
        return startPosition;
    }
    @Override
    public Position getEndPosition() {
        return endPosition;
    }
}
