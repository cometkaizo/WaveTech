package me.cometkaizo.wavetech.analyzer;

import me.cometkaizo.wavetech.analyzer.diagnostics.Diagnostic;
import me.cometkaizo.wavetech.parser.structures.CompilationUnit;

import java.util.ArrayList;
import java.util.List;

public class Analyzer {

    public Analyzer(boolean verbose) {

    }

    public List<Diagnostic> analyze(CompilationUnit compilationUnit) {
        List<Diagnostic> problems = new ArrayList<>(3);
        compilationUnit.getAnalyzer().analyze(problems);
        return problems;
    }

}
