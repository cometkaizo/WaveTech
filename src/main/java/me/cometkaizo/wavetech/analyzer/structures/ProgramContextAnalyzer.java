package me.cometkaizo.wavetech.analyzer.structures;

import me.cometkaizo.wavetech.analyzer.ResourcePool;
import me.cometkaizo.wavetech.analyzer.diagnostics.Diagnostic;

import java.util.List;

public interface ProgramContextAnalyzer extends StructureAnalyzer {

    /**
     * If not already analyzed, analyzes the current structure and adds any problems into the list.
     * @param problems the list of problems (can be empty)
     * @param resources the resources available at the current point
     * @throws IllegalStateException If the current structure is in an invalid or incomplete state.
     */
    void analyze(List<Diagnostic> problems, ResourcePool resources);

}
