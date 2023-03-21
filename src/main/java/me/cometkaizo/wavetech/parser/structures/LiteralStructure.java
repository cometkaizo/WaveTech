package me.cometkaizo.wavetech.parser.structures;

import me.cometkaizo.wavetech.analyzer.structures.EmptyAnalyzer;
import me.cometkaizo.wavetech.analyzer.structures.StructureAnalyzer;
import me.cometkaizo.wavetech.lexer.tokens.Token;
import me.cometkaizo.wavetech.lexer.tokens.types.Literal;
import me.cometkaizo.wavetech.syntaxes.AbstractSyntaxStructure;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class LiteralStructure extends AbstractSyntaxStructure<StructureAnalyzer> {

    public Literal type;
    public Object value;

    @Override
    public void store(String label, Object object) throws IllegalArgumentException {
        if ("token".equals(label)) {
            var token = (Token) object;

            type = (Literal) token.getType();
            value = token.getValue();
        }
    }

    @Override
    public String toString() {
        return "LiteralStructure{" +
                "type=" + type +
                ", value=" + value +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LiteralStructure that = (LiteralStructure) o;
        return type == that.type && Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, value);
    }

    @Override
    protected @NotNull StructureAnalyzer createAnalyzer() {
        return new EmptyAnalyzer();
    }
}
