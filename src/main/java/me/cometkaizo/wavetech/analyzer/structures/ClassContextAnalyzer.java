package me.cometkaizo.wavetech.analyzer.structures;

import me.cometkaizo.wavetech.analyzer.ClassResourcePool;
import me.cometkaizo.wavetech.analyzer.diagnostics.Diagnostic;

import java.util.List;

public interface ClassContextAnalyzer extends StructureAnalyzer {

    void analyze(List<Diagnostic> problems, ClassResourcePool resources);

}
