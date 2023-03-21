package me.cometkaizo.wavetech.parser.structures;

import me.cometkaizo.wavetech.analyzer.structures.MethodContextAnalyzer;
import me.cometkaizo.wavetech.syntaxes.SyntaxStructure;
import org.jetbrains.annotations.NotNull;

public interface MethodStatement extends SyntaxStructure {
    @Override
    @NotNull MethodContextAnalyzer getAnalyzer();

}
