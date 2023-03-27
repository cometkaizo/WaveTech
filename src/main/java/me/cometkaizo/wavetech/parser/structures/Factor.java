package me.cometkaizo.wavetech.parser.structures;

import me.cometkaizo.util.CollectionUtils;
import me.cometkaizo.wavetech.analyzer.structures.FactorAnalyzer;
import me.cometkaizo.wavetech.analyzer.structures.ProgramContextAnalyzer;
import me.cometkaizo.wavetech.syntaxes.AbstractSyntaxStructure;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class Factor extends AbstractSyntaxStructure<ProgramContextAnalyzer> {

    // exactly one of these will be non-null
    public LiteralStructure value; // a literal such as 0.1, 3, 5d, or "string"
    public ResourceAccessor<?> resourceAccessor;

    public ClassStructure type;


    protected @NotNull ProgramContextAnalyzer createAnalyzer() {
        return new FactorAnalyzer(this);
    }

    @Override
    public void store(String label, Object object) throws IllegalArgumentException {
        switch (label) {
            case "value" -> value = (LiteralStructure) object;
            case "resource" -> resourceAccessor = (ResourceAccessor<?>) object;
        }
    }

    @Override
    public String toString() {
        return "Factor{" +
                CollectionUtils.firstNonNull(value, resourceAccessor).orElse(null) +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Factor factor = (Factor) o;
        return Objects.equals(value, factor.value) && Objects.equals(resourceAccessor, factor.resourceAccessor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, resourceAccessor);
    }

}
