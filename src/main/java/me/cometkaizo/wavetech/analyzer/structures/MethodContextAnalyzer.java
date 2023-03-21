package me.cometkaizo.wavetech.analyzer.structures;

import me.cometkaizo.wavetech.analyzer.MethodResourcePool;
import me.cometkaizo.wavetech.analyzer.diagnostics.Diagnostic;

import java.util.List;

public interface MethodContextAnalyzer extends StructureAnalyzer {

    void analyze(List<Diagnostic> problems, MethodResourcePool resources);

}
