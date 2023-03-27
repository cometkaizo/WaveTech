package me.cometkaizo.wavetech.parser.structures;

import me.cometkaizo.wavetech.analyzer.structures.ForLoopAnalyzer;
import me.cometkaizo.wavetech.analyzer.structures.MethodContextAnalyzer;
import me.cometkaizo.wavetech.syntaxes.AbstractSyntaxStructure;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class ForLoopStatement extends AbstractSyntaxStructure<MethodContextAnalyzer> implements MethodStatement {

    public VariableDeclaration varInit;
    public Expression terminationCondition;
    public VariableAssignation varUpdate;
    public MethodBlock body;

    @Override
    public void store(String label, Object object) throws IllegalArgumentException {
        switch (label) {
            case "init" -> varInit = cast(object);
            case "termination" -> terminationCondition = cast(object);
            case "update" -> varUpdate = cast(object);
            case "body" -> body = cast(object);
        }
    }

    @Override
    @NotNull
    protected MethodContextAnalyzer createAnalyzer() {
        return new ForLoopAnalyzer(this);
    }

    @Override
    public String toString() {
        return "ForLoopStatement{" +
                "varInit=" + varInit +
                ", terminationCondition=" + terminationCondition +
                ", varUpdate=" + varUpdate +
                ", body=\n" + body +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ForLoopStatement that = (ForLoopStatement) o;
        return Objects.equals(varInit, that.varInit) && Objects.equals(terminationCondition, that.terminationCondition) && Objects.equals(varUpdate, that.varUpdate) && Objects.equals(body, that.body);
    }

    @Override
    public int hashCode() {
        return Objects.hash(varInit, terminationCondition, varUpdate, body);
    }

}
