package me.cometkaizo.wavetech.parser.structures;

import me.cometkaizo.wavetech.analyzer.ResourcePool;
import me.cometkaizo.wavetech.analyzer.diagnostics.Diagnostic;
import me.cometkaizo.wavetech.syntaxes.ResourceStructure;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface EvaluableResource extends ResourceStructure {

    @Nullable
    ClassStructure getTypeOrAnalyze(ResourcePool resources, List<Diagnostic> problems);

    Identifier getTypeName();
}
