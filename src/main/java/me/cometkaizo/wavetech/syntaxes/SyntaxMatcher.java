package me.cometkaizo.wavetech.syntaxes;

import me.cometkaizo.wavetech.parser.diagnostics.ParseDiagnostic;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface SyntaxMatcher {

    @Nullable
    <T extends SyntaxStructure> SyntaxStructure tryMatch(Syntax<T> syntax, @Nullable SyntaxStructure parent, List<ParseDiagnostic> problems);

}
