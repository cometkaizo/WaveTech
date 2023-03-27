package me.cometkaizo.wavetech.parser.structures;

import me.cometkaizo.wavetech.analyzer.MethodResourcePool;
import me.cometkaizo.wavetech.analyzer.diagnostics.Diagnostic;
import me.cometkaizo.wavetech.analyzer.diagnostics.IncompatibleTypesDiagnostic;
import me.cometkaizo.wavetech.analyzer.structures.MethodContextAnalyzer;
import me.cometkaizo.wavetech.parser.BuiltInClasses;

import java.util.List;

class IfStatementAnalyzer implements MethodContextAnalyzer {
    private final IfStatement ifStatement;
    boolean analyzed = false;

    public IfStatementAnalyzer(IfStatement ifStatement) {
        this.ifStatement = ifStatement;
    }

    @Override
    public void analyze(List<Diagnostic> problems, MethodResourcePool resources) {
        if (analyzed) return;
        analyzed = true;
        throwIfIllegalState();

        var copiedResources = resources.copy();
        ifStatement.condition.getAnalyzer().analyze(problems, copiedResources);
        checkConditionType(problems);
        ifStatement.body.getAnalyzer().analyze(problems, copiedResources);
    }

    private void throwIfIllegalState() {
        if (ifStatement.condition == null)
            throw new IllegalStateException("Condition is null");
        if (ifStatement.body == null)
            throw new IllegalStateException("Body is null");
    }

    private void checkConditionType(List<Diagnostic> problems) {
        if (ifStatement.condition.type != BuiltInClasses.BOOLEAN)
            problems.add(new IncompatibleTypesDiagnostic(ifStatement.condition.type,
                    BuiltInClasses.BOOLEAN,
                    ifStatement.condition));
    }
}
