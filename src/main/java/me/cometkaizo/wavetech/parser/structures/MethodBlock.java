package me.cometkaizo.wavetech.parser.structures;

import me.cometkaizo.wavetech.analyzer.MethodResourcePool;
import me.cometkaizo.wavetech.analyzer.diagnostics.Diagnostic;
import me.cometkaizo.wavetech.analyzer.structures.MethodContextAnalyzer;
import me.cometkaizo.wavetech.syntaxes.AbstractSyntaxStructure;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MethodBlock extends AbstractSyntaxStructure<MethodContextAnalyzer> implements MethodStatement {
    public List<MethodStatement> statements = new ArrayList<>(1);

    @Override
    public void store(String label, Object object) throws IllegalArgumentException {
        if (!"content".equals(label)) return;

        if (object instanceof MethodStatement s) statements.add(s);
    }

    @Override
    @NotNull
    protected MethodContextAnalyzer createAnalyzer() {
        return new MethodContextAnalyzer() {
            boolean analyzed = false;
            @Override
            public void analyze(List<Diagnostic> problems, MethodResourcePool resources) {
                if (analyzed) return;
                MethodResourcePool copiedResources = resources.copy();
                for (var statement : statements) statement.getAnalyzer().analyze(problems, copiedResources);
                analyzed = true;
            }
        };
    }

    @Override
    public String toString() {
        return "Block{" +
                "statements=" + statements +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MethodBlock block = (MethodBlock) o;
        return Objects.equals(statements, block.statements);
    }

    @Override
    public int hashCode() {
        return Objects.hash(statements);
    }
}
