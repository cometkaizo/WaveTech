package me.cometkaizo.wavetech.parser;

import me.cometkaizo.util.PeekingIterator;
import me.cometkaizo.wavetech.lexer.tokens.Token;
import me.cometkaizo.wavetech.parser.diagnostics.ParseDiagnostic;
import me.cometkaizo.wavetech.parser.structures.CompilationUnit;
import me.cometkaizo.wavetech.parser.syntaxes.CompilationUnitSyntax;
import me.cometkaizo.wavetech.syntaxes.SyntaxMatcherImpl;

import java.util.ArrayList;
import java.util.List;

public class Parser {

    public Result parse(List<Token> tokens) {
        var matcher = new SyntaxMatcherImpl(PeekingIterator.of(tokens));
        var baseSyntax = CompilationUnitSyntax.getInstance();

        List<ParseDiagnostic> problemNodes = new ArrayList<>(1);
        var compilationUnit = (CompilationUnit) matcher.tryMatch(baseSyntax, null, problemNodes);
        return new Result(problemNodes, compilationUnit);
    }

    public record Result(List<ParseDiagnostic> problems, CompilationUnit compilationUnit) {
        public boolean isSuccessful() {
            return compilationUnit != null;
        }
    }

}
