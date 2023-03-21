package me.cometkaizo.wavetech.parser.structures;

import me.cometkaizo.wavetech.analyzer.structures.CompilationUnitAnalyzer;
import me.cometkaizo.wavetech.syntaxes.AbstractSyntaxStructure;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class CompilationUnit extends AbstractSyntaxStructure<CompilationUnitAnalyzer> {
    public ClassStructure classStructure;

    @Override
    public void store(String label, Object object) {
        if ("class".equals(label)) {
            classStructure = (ClassStructure) object;
        }
    }

    @Override
    @NotNull
    protected CompilationUnitAnalyzer createAnalyzer() {
        return new CompilationUnitAnalyzer(this);
    }


    @Override
    public String toString() {
        return "CompilationUnit{" +
                "class=" + classStructure +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CompilationUnit that = (CompilationUnit) o;
        return Objects.equals(classStructure, that.classStructure);
    }

    @Override
    public int hashCode() {
        return Objects.hash(classStructure);
    }

}
