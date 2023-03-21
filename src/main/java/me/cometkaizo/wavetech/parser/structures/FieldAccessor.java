package me.cometkaizo.wavetech.parser.structures;

import me.cometkaizo.wavetech.analyzer.diagnostics.Diagnostic;
import me.cometkaizo.wavetech.analyzer.diagnostics.UnknownFieldDiagnostic;
import me.cometkaizo.wavetech.analyzer.structures.ResourceAnalyzer;
import me.cometkaizo.wavetech.syntaxes.AbstractSyntaxStructure;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class FieldAccessor extends AbstractSyntaxStructure<ResourceAnalyzer> implements ResourceAccessor<Identifier> {

    public Identifier variableName;

    @Override
    public Identifier getSignature() {
        return variableName;
    }

    @Override
    public void store(String label, Object object) {
        if ("variableName".equals(label)) variableName = (Identifier) object;
    }

    @Override
    @NotNull
    protected ResourceAnalyzer createAnalyzer() {
        return new ResourceAnalyzer(this);
    }

    @Override
    public Diagnostic getNoResourceDiagnostic() {
        return new UnknownFieldDiagnostic(this);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + '{' +
                "variableName=" + variableName +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FieldAccessor that = (FieldAccessor) o;
        return Objects.equals(variableName, that.variableName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(variableName);
    }
}
