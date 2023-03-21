package me.cometkaizo.wavetech.analyzer.structures;

import me.cometkaizo.wavetech.analyzer.ResourcePool;
import me.cometkaizo.wavetech.analyzer.diagnostics.Diagnostic;
import me.cometkaizo.wavetech.parser.structures.ResourceAccessor;

import java.util.List;

public class ResourceAnalyzer implements ProgramContextAnalyzer {
    boolean analyzed = false;
    private final ResourceAccessor<?> resourceAccessor;

    public ResourceAnalyzer(ResourceAccessor<?> resourceAccessor) {
        this.resourceAccessor = resourceAccessor;
    }


    public void analyze(List<Diagnostic> problems, ResourcePool resources) {
        if (analyzed) return;

        if (!resources.hasResourceOrAnalyze(resourceAccessor, problems)) {
            problems.add(resourceAccessor.getNoResourceDiagnostic());
        }

        analyzed = true;
    }
}
