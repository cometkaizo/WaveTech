package me.cometkaizo.wavetech.analyzer;

import me.cometkaizo.wavetech.analyzer.diagnostics.Diagnostic;
import me.cometkaizo.wavetech.parser.structures.ResourceAccessor;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface ResourcePool {
    boolean hasResourceOrAnalyze(ResourceAccessor<?> resourceAccessor, List<Diagnostic> problems);
    @Nullable
    Object getResourceOrAnalyze(ResourceAccessor<?> resourceAccessor, List<Diagnostic> problems);

    ResourcePool copy();
}