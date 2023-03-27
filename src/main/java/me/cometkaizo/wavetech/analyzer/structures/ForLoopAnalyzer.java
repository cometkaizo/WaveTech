package me.cometkaizo.wavetech.analyzer.structures;

import me.cometkaizo.wavetech.analyzer.MethodResourcePool;
import me.cometkaizo.wavetech.analyzer.diagnostics.Diagnostic;
import me.cometkaizo.wavetech.analyzer.diagnostics.IncompatibleTypesDiagnostic;
import me.cometkaizo.wavetech.parser.BuiltInClasses;
import me.cometkaizo.wavetech.parser.structures.ForLoopStatement;

import java.util.List;

public class ForLoopAnalyzer implements MethodContextAnalyzer {
    private final ForLoopStatement forLoop;
    boolean analyzed = false;

    public ForLoopAnalyzer(ForLoopStatement forLoop) {
        this.forLoop = forLoop;
    }

    @Override
    public void analyze(List<Diagnostic> problems, MethodResourcePool resources) {
        if (analyzed) return;
        analyzed = true;
        throwIfIllegalState();

        var copiedResources = resources.copy();

        if (null != forLoop.varInit) forLoop.varInit.getAnalyzer().analyze(problems, copiedResources);
        if (null != forLoop.terminationCondition) {
            forLoop.terminationCondition.getAnalyzer().analyze(problems, copiedResources);
            checkTerminationType(problems);
        }
        if (null != forLoop.varUpdate)
            forLoop.varUpdate.getAnalyzer().analyze(problems, copiedResources);
        forLoop.body.getAnalyzer().analyze(problems, copiedResources);

    }

    private void throwIfIllegalState() {
        if (forLoop.body == null)
            throw new IllegalStateException("Body is null");
    }

    private void checkTerminationType(List<Diagnostic> problems) {
        if (forLoop.terminationCondition.type != BuiltInClasses.BOOLEAN)
            problems.add(new IncompatibleTypesDiagnostic(forLoop.terminationCondition.type,
                    BuiltInClasses.BOOLEAN,
                    forLoop.terminationCondition));
    }
}
