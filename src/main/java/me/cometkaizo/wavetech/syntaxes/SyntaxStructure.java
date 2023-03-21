package me.cometkaizo.wavetech.syntaxes;

import me.cometkaizo.wavetech.analyzer.structures.StructureAnalyzer;
import me.cometkaizo.wavetech.lexer.tokens.Position;
import org.jetbrains.annotations.NotNull;

public interface SyntaxStructure {

    /**
     * Potentially stores the object according to the label.
     * @param label the label to identify the object
     * @param object the value to store
     * @throws ClassCastException If the object is not the correct type associated with the label.
     */
    void store(String label, Object object);


    @NotNull StructureAnalyzer getAnalyzer();



    @SuppressWarnings("unchecked")
    default <T> T cast(Object object) {
        return (T) object;
    }

    @Override
    boolean equals(Object obj);

    @Override
    int hashCode();

    void setStartPosition(Position startPosition);
    void setEndPosition(Position endPosition);
    Position getStartPosition();
    Position getEndPosition();
}
