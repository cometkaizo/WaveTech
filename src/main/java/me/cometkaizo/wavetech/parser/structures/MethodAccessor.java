package me.cometkaizo.wavetech.parser.structures;

import me.cometkaizo.wavetech.analyzer.diagnostics.Diagnostic;
import me.cometkaizo.wavetech.analyzer.diagnostics.UnknownMethodDiagnostic;
import me.cometkaizo.wavetech.analyzer.structures.MethodAccessorAnalyzer;
import me.cometkaizo.wavetech.syntaxes.AbstractSyntaxStructure;
import me.cometkaizo.wavetech.syntaxes.SyntaxStructure;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MethodAccessor extends AbstractSyntaxStructure<MethodAccessorAnalyzer> implements ResourceAccessor<MethodStructure.Signature>, MethodStatement {

    public final SyntaxStructure parent;
    public Identifier methodName;
    public List<Expression> parameters = new ArrayList<>(2);
    public MethodStructure.Signature signature;

    public MethodAccessor(SyntaxStructure parent) {
        this.parent = parent;
    }


    @Override
    public MethodStructure.Signature getSignature() {
        return signature;
    }

    @Override
    public void store(String label, Object object) {
        switch (label) {
            case "name" -> methodName = (Identifier) object;
            case "parameter" -> parameters.add((Expression) object);
        }
    }

    @Override
    @NotNull
    protected MethodAccessorAnalyzer createAnalyzer() {
        return new MethodAccessorAnalyzer(this);
    }

    @Override
    public Diagnostic getNoResourceDiagnostic() {
        return new UnknownMethodDiagnostic(this);
    }

    @Override
    public String toString() {
        return "MethodAccessor{" +
                "name=" + methodName +
                ", parameters=" + parameters +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MethodAccessor that = (MethodAccessor) o;
        return Objects.equals(methodName, that.methodName) && Objects.equals(parameters, that.parameters);
    }

    @Override
    public int hashCode() {
        return Objects.hash(methodName, parameters);
    }
}
