package me.cometkaizo.wavetech.parser.structures;

import me.cometkaizo.wavetech.analyzer.diagnostics.Diagnostic;
import me.cometkaizo.wavetech.analyzer.structures.ResourceAnalyzer;
import me.cometkaizo.wavetech.syntaxes.SyntaxStructure;
import org.jetbrains.annotations.NotNull;

public interface ResourceAccessor<T> extends SyntaxStructure {

    T getSignature();

    @Override
    @NotNull
    ResourceAnalyzer getAnalyzer();

    Diagnostic getNoResourceDiagnostic();

}
