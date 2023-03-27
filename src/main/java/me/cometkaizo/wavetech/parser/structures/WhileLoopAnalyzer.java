package me.cometkaizo.wavetech.parser.structures;

import me.cometkaizo.wavetech.analyzer.MethodResourcePool;
import me.cometkaizo.wavetech.analyzer.diagnostics.Diagnostic;
import me.cometkaizo.wavetech.analyzer.diagnostics.IncompatibleTypesDiagnostic;
import me.cometkaizo.wavetech.analyzer.structures.MethodContextAnalyzer;
import me.cometkaizo.wavetech.parser.BuiltInClasses;

import java.util.List;

class WhileLoopAnalyzer implements MethodContextAnalyzer {
    private final WhileLoopStatement whileLoop;
    boolean analyzed = false;

    public WhileLoopAnalyzer(WhileLoopStatement whileLoop) {
        this.whileLoop = whileLoop;
    }

    @Override
    public void analyze(List<Diagnostic> problems, MethodResourcePool resources) {
        if (analyzed) return;
        analyzed = true;
        throwIfIllegalState();

        var copiedResources = resources.copy();

        whileLoop.condition.getAnalyzer().analyze(problems, copiedResources);
        checkConditionType(problems);
        whileLoop.body.getAnalyzer().analyze(problems, copiedResources);
    }

    private void checkConditionType(List<Diagnostic> problems) {
        if (whileLoop.condition.type != BuiltInClasses.BOOLEAN)
            problems.add(new IncompatibleTypesDiagnostic(whileLoop.condition.type,
                    BuiltInClasses.BOOLEAN,
                    whileLoop.condition));
    }

    private void throwIfIllegalState() {
        if (whileLoop.condition == null)
            throw new IllegalStateException("Condition is null");
        if (whileLoop.body == null)
            throw new IllegalStateException("Body is null");
    }
}
