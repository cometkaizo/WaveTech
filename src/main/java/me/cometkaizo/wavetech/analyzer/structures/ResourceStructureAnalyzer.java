package me.cometkaizo.wavetech.analyzer.structures;

import me.cometkaizo.wavetech.analyzer.diagnostics.Diagnostic;

import java.util.List;

public interface ResourceStructureAnalyzer extends StructureAnalyzer {

    /**
     * Analyzes the current structure and adds any problems into the list.
     * @param problems the list of problems (can be empty)
     * @throws IllegalStateException If the current structure is in an invalid or incomplete state.
     */
    void analyze(List<Diagnostic> problems);

}
