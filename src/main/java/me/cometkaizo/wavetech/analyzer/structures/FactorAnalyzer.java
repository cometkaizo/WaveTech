package me.cometkaizo.wavetech.analyzer.structures;

import me.cometkaizo.wavetech.analyzer.ResourcePool;
import me.cometkaizo.wavetech.analyzer.diagnostics.Diagnostic;
import me.cometkaizo.wavetech.parser.structures.EvaluableResource;
import me.cometkaizo.wavetech.parser.structures.Factor;

import java.util.List;

public class FactorAnalyzer implements ProgramContextAnalyzer {
    private final Factor factor;
    boolean analyzed = false;

    public FactorAnalyzer(Factor factor) {
        this.factor = factor;
    }

    @Override
    public void analyze(List<Diagnostic> problems, ResourcePool resources) {
        if (analyzed) return;
        analyzed = true;

        if (factor.value != null) {
            factor.type = factor.value.type.getClassType();
            return;
        }

        factor.resourceAccessor.getAnalyzer().analyze(problems, resources);
        EvaluableResource evaluableResource = (EvaluableResource) resources.getResourceOrAnalyze(factor.resourceAccessor, problems);

        if (evaluableResource != null) {
            factor.type = evaluableResource.getTypeOrAnalyze(resources, problems);
        }
    }
}
