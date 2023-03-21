package me.cometkaizo.wavetech.analyzer.structures;

import me.cometkaizo.wavetech.analyzer.diagnostics.Diagnostic;
import me.cometkaizo.wavetech.parser.structures.CompilationUnit;

import java.util.List;

public class CompilationUnitAnalyzer implements ResourceStructureAnalyzer {
    boolean analyzed = false;
    private final CompilationUnit compilationUnit;

    public CompilationUnitAnalyzer(CompilationUnit compilationUnit) {
        this.compilationUnit = compilationUnit;
    }

    @Override
    public void analyze(List<Diagnostic> problems) {
        if (analyzed) return;

        var classAnalyzer = compilationUnit.classStructure.getAnalyzer();
        classAnalyzer.analyze(problems);

        analyzed = true;
    }

}
