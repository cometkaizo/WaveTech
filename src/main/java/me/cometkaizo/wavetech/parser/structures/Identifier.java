package me.cometkaizo.wavetech.parser.structures;

import me.cometkaizo.wavetech.analyzer.structures.ResourceStructureAnalyzer;
import me.cometkaizo.wavetech.lexer.tokens.Token;
import me.cometkaizo.wavetech.syntaxes.AbstractSyntaxStructure;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class Identifier extends AbstractSyntaxStructure<ResourceStructureAnalyzer> {

    public String name;

    public Identifier(String name) {
        this.name = name;
    }

    public Identifier() {

    }

    @Override
    public void store(String label, Object object) {
        if ("name".equals(label)) {
            name = (String) ((Token) object).getValue();
        }
    }

    @Override
    @NotNull
    protected ResourceStructureAnalyzer createAnalyzer() {
        return problems -> {
            if (name == null || name.isBlank())
                throw new IllegalStateException("Illegal identifier name '" + name + "'");
        };
    }

    @Override
    public String toString() {
        return "IdentifierStructure{" +
                "name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Identifier that = (Identifier) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
