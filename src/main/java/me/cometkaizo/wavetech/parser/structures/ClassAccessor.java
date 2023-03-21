package me.cometkaizo.wavetech.parser.structures;

import me.cometkaizo.wavetech.analyzer.diagnostics.Diagnostic;
import me.cometkaizo.wavetech.analyzer.diagnostics.UnknownClassDiagnostic;
import me.cometkaizo.wavetech.analyzer.structures.ResourceAnalyzer;
import me.cometkaizo.wavetech.syntaxes.AbstractSyntaxStructure;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class ClassAccessor extends AbstractSyntaxStructure<ResourceAnalyzer> implements ResourceAccessor<Identifier> {

    public Identifier className;

    @Override
    public Identifier getSignature() {
        return className;
    }

    @Override
    public void store(String label, Object object) {
        if ("className".equals(label)) className = (Identifier) object;
    }

    @Override
    @NotNull
    protected ResourceAnalyzer createAnalyzer() {
        return new ResourceAnalyzer(this);
    }

    @Override
    public Diagnostic getNoResourceDiagnostic() {
        return new UnknownClassDiagnostic(this);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + '{' +
                "className=" + className +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClassAccessor that = (ClassAccessor) o;
        return Objects.equals(className, that.className);
    }

    @Override
    public int hashCode() {
        return Objects.hash(className);
    }
}
